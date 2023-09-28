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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.jdo.annotations.Transactional;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private QuerydslRepository queryCtx;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    public static final LocalTime DEFAULT_BOOKING_START_TIME = LocalTime.of(8, 0);;

    public static final LocalTime DEFAULT_BOOKING_END_TIME = LocalTime.of(17, 0);;

    @Override//查詢所有預定會議室訂單(完成)
    public List<Booking> findAllBooking() {
        return bookingRepository.findAll();
    }

    @Override//分頁查詢
    public List<Booking> findFilteredBookings(int page, int size, String roomId, String userId, String startTime, String endTime, String id) {
        Specification<Booking> spec = Specification.where(null);

        if (id != null) {
            assert spec != null;
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("id"), id)
            );
        }
        if (roomId != null) {
            assert spec != null;
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("roomId"), roomId)
            );
        }
        if (userId != null) {
            assert spec != null;
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("userId"), userId)
            );
        }
        if (startTime != null) {
            assert spec != null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDate = LocalDateTime.parse(startTime, formatter);
            Date startAsDate = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());

            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), startAsDate)
            );
        }
        if (endTime != null) {
            assert spec != null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime endDate = LocalDateTime.parse(endTime, formatter);
            Date endAsDate = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());

            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), endAsDate)
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


    @Override//登入後取得個人所有訂單(完成)
    public Option<List<Booking>> getOwnBookingByUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Option.none();
        }

        //取得有權限的帳號
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
        //取得訂單是否存在
        Booking existed = getBookingById(id).getOrElseThrow(() -> new BadRequestException(ApiErrorCode.BOOKING_NOT_FOUND));
        bookingRepository.delete(existed);
    }

    @Override//會議室訂單更新(完成)
    @Transactional
    public Option<Booking> updateBooking(String id, BookingUpdates updates) {
        //取得訂單是否存在
        Booking existed = getBookingById(id).getOrElseThrow(() -> new BadRequestException(ApiErrorCode.BOOKING_NOT_FOUND));
        if(updates.getEndTime().isBefore(updates.
        getStartTime())){
            throw new BadRequestException(ApiErrorCode.START_TIME_AFTER_END_TIME);
        }
        //設定更新參數
        existed.setStartTime(updates.getStartTime());
        existed.setEndTime(updates.getEndTime());
        existed.setRoomId(updates.getRoomId());
        return Option.of(bookingRepository.save(existed));

    }


    @Override//預定會議室(完成)
    @Transactional
    public Option<Booking> createBooking(BookingCreation creation) throws NotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {

            String userId = authentication.getName();
            //檢查會議室是否存在
            Room room = roomRepository.findById(creation.getRoomId())
                    .orElseThrow(() -> new NotFoundException("Room not found"));

            // 會議室的預定 (今天以後)
            LocalDateTime todayStart = LocalDate.now().atStartOfDay();
            List<Booking> existingBookings = bookingRepository.findByRoomIdAndStartTimeAfter(room.getId(), todayStart);

            // 檢查要設定的時間是否有重疊
            boolean isOverlap = existingBookings.stream()
                    .anyMatch(existingBooking ->
                            (creation.getStartTime().isBefore(existingBooking.getEndTime()) &&
                                    creation.getEndTime().isAfter(existingBooking.getStartTime())));

            //如果有重疊拋出錯誤
            if (isOverlap) {
                throw new RuntimeException("Booking time overlaps with existing bookings");
            }

            //LocalDateTime轉成LocalTime
            LocalTime startTimeInTaipei = creation.getStartTime().toLocalTime();
            LocalTime endTimeInTaipei = creation.getEndTime().toLocalTime();

            if (startTimeInTaipei.isBefore(LocalTime.of(8, 0)) || endTimeInTaipei.isAfter(LocalTime.of(17, 0))) {
                throw new BadRequestException(ApiErrorCode.Booking_time_must_be_between_8AM_and_5_PM);
            }

            // SAVE設定存取參數
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
    public Option<List<Map<String, String>>> getAvailableTimeSlots(int roomId) {

        LocalDateTime startDateTime = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(30).withDayOfMonth(1).with(LocalTime.MAX);

        List<Booking> bookedSlots = bookingRepository.findBookingsByRoomIdAndTimeRange(
                roomId,
                //轉成LocalDateTime
                startDateTime,
                endDateTime
        );

        List<Map<String, LocalDateTime>> availableTimeSlots = calculateAvailableTimeSlots(
                bookedSlots, startDateTime
        );

        List<Map<String, String>> formattedTimeSlots = convertToFormattedString(availableTimeSlots);

        return Option.of(formattedTimeSlots);
    }

    private List<Map<String, LocalDateTime>> calculateAvailableTimeSlots(
             List<Booking> bookedSlots, LocalDateTime taiwanNow) {

        List<Map<String, LocalDateTime>> availableTimeSlots = new ArrayList<>();
        LocalDateTime currentDateTime = taiwanNow.with(DEFAULT_BOOKING_START_TIME);
        LocalDateTime endDateTime = taiwanNow.plusMonths(1);

        if (taiwanNow.toLocalTime().isAfter(DEFAULT_BOOKING_END_TIME)) {
            currentDateTime = taiwanNow.plusDays(1).with(DEFAULT_BOOKING_START_TIME);
        } else if (taiwanNow.toLocalTime().isAfter(DEFAULT_BOOKING_START_TIME)) {
            currentDateTime = taiwanNow.truncatedTo(ChronoUnit.HOURS).plusHours(1);
        }

        while (currentDateTime.isBefore(endDateTime)) {
            LocalDateTime slotStartTime = currentDateTime;
            LocalDateTime slotEndTime = currentDateTime.plusHours(1);
            if (slotStartTime.toLocalTime().isAfter(DEFAULT_BOOKING_START_TIME.minusMinutes(1)) &&
                    slotEndTime.toLocalTime().isBefore(DEFAULT_BOOKING_END_TIME.plusMinutes(1))) {
                boolean isBooked = bookedSlots.stream().anyMatch(booking -> {
                    LocalDateTime startInstant = booking.getStartTime();
                    LocalDateTime endInstant = booking.getEndTime();

                    return (startInstant.isBefore(slotEndTime) && endInstant.isAfter(slotStartTime));
                });

                if (!isBooked) {
                    Map<String, LocalDateTime> timeSlot = new HashMap<>();
                    timeSlot.put("startTime", slotStartTime);
                    timeSlot.put("endTime", slotEndTime);
                    availableTimeSlots.add(timeSlot);
                }
            }
            currentDateTime = currentDateTime.plusHours(1);

            if (currentDateTime.toLocalTime().isAfter(DEFAULT_BOOKING_END_TIME)) {
                currentDateTime = LocalDateTime.of(
                        currentDateTime.toLocalDate().plusDays(1),
                        DEFAULT_BOOKING_START_TIME
                );
            }
        }
        return availableTimeSlots;
    }

    public List<Map<String, String>> convertToFormattedString(List<Map<String, LocalDateTime>> originalList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Map<String, String>> newList = new ArrayList<>();

        for (Map<String, LocalDateTime> originalMap : originalList) {
            Map<String, String> newMap = new HashMap<>();
            for (Map.Entry<String, LocalDateTime> entry : originalMap.entrySet()) {
                newMap.put(entry.getKey(), entry.getValue().format(formatter));
            }
            newList.add(newMap);
        }
        return newList;
    }
}