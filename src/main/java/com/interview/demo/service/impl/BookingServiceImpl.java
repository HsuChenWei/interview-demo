package com.interview.demo.service.impl;


import com.interview.demo.entity.Booking;
import com.interview.demo.entity.QBooking;
import com.interview.demo.entity.Room;
import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.model.Booking.BookingCreation;
import com.interview.demo.model.Booking.BookingUpdates;
import com.interview.demo.repository.BookingRepository;
import com.interview.demo.repository.RoomRepository;
import com.interview.demo.repository.querydsl.QuerydslRepository;
import com.interview.demo.service.BookingService;
import io.vavr.control.Option;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.jdo.annotations.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private QuerydslRepository queryCtx;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override//查詢所有預定會議室訂單(完成)
    public List<Booking> findAllBooking() {
        return bookingRepository.findAll();
    }



    @Override//分頁查詢
    public List<Booking> findFilteredBookings(int page, int size, String roomId, String userId, String startTime, String endTime, String id) {
        Specification<Booking> spec = Specification.where(null);

        if (id != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("id"), id)
            );
        }
        if (roomId != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("roomId"), roomId)
            );
        }
        if (userId != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("userId"), userId)
            );
        }
        if (startTime != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), startTime)
            );
        }
        if (endTime != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), endTime)
            );
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> bookingsPage = bookingRepository.findAll(spec, pageable);
        bookingRepository.count();

        return bookingsPage.getContent();
    }


    @Override//查詢個人所有會議室訂單(完成)
    public Option<List<Booking>> getByUserId(String userId) {
        QBooking res = QBooking.booking;
        return Option.of(queryCtx.newQuery()
                .selectFrom(res)
                .where(res.userId.eq(userId))
                .fetch());
    }

    //登入後取得個人資料(完成)
    @Override
    public Option<List<Booking>> getMySelfBookingByUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Option.none();
        }
        String currentUsername = authentication.getName();

        QBooking booking = QBooking.booking;
        List<Booking> bookings = queryCtx.newQuery()
                .selectFrom(booking)
                .where(booking.userId.eq(currentUsername))
                .fetch();

        if (!bookings.isEmpty()) {
            return Option.of(bookings);
        } else {
            return Option.none();
        }
    }


    @Override//查詢單一會議室訂單(完成)
    public Option<Booking> getBookingById(String id) {
        QBooking booking = QBooking.booking;
        return Option.of(queryCtx.newQuery()
                .selectFrom(booking)
                .where(booking.id.eq(id))
                .fetchOne());
    }

    @Override//取消會議室訂單(完成)
    @Transactional
    public void removeBookingById(String id) {
        Booking existed = getBookingById(id).getOrElseThrow(() -> new BadRequestException(ApiErrorCode.BOOKING_NOT_FOUND));
        bookingRepository.delete(existed);
    }

    @Override//會議室訂單更新(完成)
    @Transactional
    public Option<Booking> updateBooking(String id, BookingUpdates updates) {
        Booking existed = getBookingById(id).getOrElseThrow(() -> new BadRequestException(ApiErrorCode.BOOKING_NOT_FOUND));
        if(updates.getEndTime().before(updates.
        getStartTime())){
            throw new BadRequestException(ApiErrorCode.START_TIME_AFTER_END_TIME);
        }
        existed.setStartTime(updates.getStartTime());
        existed.setEndTime(updates.getEndTime());
        existed.setRoomId(updates.getRoomId());
        return Option.of(bookingRepository.save(existed));

    }

    //預定完會議室後，會員會被重複註冊(完成)
    @Override
    @Transactional
    public Option<Booking> createBooking(BookingCreation creation) throws NotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {

            String userId = authentication.getName();
            // get 會議室
            Room room = roomRepository.findById(creation.getRoomId())
                    .orElseThrow(() -> new NotFoundException("Room not found"));

            // get 會議室的預定 (今天以後)
            LocalDateTime todayStart = LocalDate.now().atStartOfDay();
            List<Booking> existingBookings = bookingRepository.findByRoomIdAndStartTimeAfter(
                    room.getId(), Timestamp.valueOf(todayStart));

            // 檢查要設定的時間是否有重疊
            boolean isOverlap = existingBookings.stream()
                    .anyMatch(existingBooking ->
                            (creation.getStartTime().before(existingBooking.getEndTime()) &&
                                    creation.getEndTime().after(existingBooking.getStartTime())));

            //如果有重疊，拋出錯誤(EXCEPTION)
            if (isOverlap) {
                throw new RuntimeException("Booking time overlaps with existing bookings");
            }

            //判斷開始時間是否在今天之前的日期
            if (creation.getStartTime().toLocalDateTime().isBefore(todayStart)) {
                throw new BadRequestException(ApiErrorCode.START_DATE_BEFORE_TODAY);
            }

            //判斷結束時間是否早於開始時間
            if (creation.getEndTime().before(creation.getStartTime())) {
                throw new BadRequestException(ApiErrorCode.START_TIME_AFTER_END_TIME);
            }

            // SAVE
            Booking b = new Booking();
            b.setUserId(userId);
            b.setRoomId(creation.getRoomId());
            b.setStartTime(creation.getStartTime());
            b.setEndTime(creation.getEndTime());
            return Option.of(bookingRepository.save(b));
        }else {
            throw new BadRequestException(ApiErrorCode.USER_IS_NOT_AUTHENTICATED);
        }
    }

    @Override
    public Option<Booking> getRoomId(String roomId) {
        QBooking booking = QBooking.booking;
        return Option.of(queryCtx.newQuery()
                .selectFrom(booking)
                .where(booking.roomId.eq(Integer.valueOf(roomId)))
                .fetchOne());
    }

}