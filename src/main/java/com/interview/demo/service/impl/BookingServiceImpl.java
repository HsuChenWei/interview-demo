package com.interview.demo.service.impl;


import com.interview.demo.entity.Booking;
import com.interview.demo.entity.QBooking;
import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.model.Booking.BookingCreation;
import com.interview.demo.model.Booking.BookingUpdates;
import com.interview.demo.repository.BookingRepository;
import com.interview.demo.repository.RoomRepository;
import com.interview.demo.repository.UserRepository;
import com.interview.demo.repository.querydsl.QuerydslRepository;
import com.interview.demo.service.BookingService;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {


    @Autowired
    private QuerydslRepository queryCtx;

    @Autowired
    private BookingRepository bookingRepository;

    @Override//查詢所有預定會議室訂單(完成)
    public List<Booking> findAllBooking() {
        return bookingRepository.findAll();
    }

    @Override//查詢個人所有會議室訂單(完成)
    public Option<List<Booking>> getByUserId(String userId) {
        QBooking res = QBooking.booking;
        return Option.of(queryCtx.newQuery()
                .selectFrom(res)
                .where(res.userId.eq(userId))
                .fetch());
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
    public void removeBookingById(String id) {
        Booking existed = getBookingById(id).getOrElseThrow(() -> new BadRequestException(ApiErrorCode.BOOKING_NOT_FOUND));
        bookingRepository.delete(existed);
    }

    @Override//會議室訂單更新(完成)
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

    //預定完會議室後，會員會被重複註冊(未完成)
    @Override
    public Option<Booking> createBooking(BookingCreation creation) {
        Booking b = new Booking();
        b.setUserId("1127959317338484736");//ID先寫死之後再改成使用者登入的動態寫法
        b.setRoomId(creation.getRoomId());
        b.setStartTime(creation.getStartTime());
        b.setEndTime(creation.getEndTime());
        if (b.getEndTime().before(b.getStartTime())) {
            throw new BadRequestException(ApiErrorCode.START_TIME_AFTER_END_TIME);
        }
        return Option.of(bookingRepository.save(b));
    }


    //        Room bookingRoom = theBooking.getRoom();
//        User bookingUser = theBooking.getUser();
//
//        if (bookingRoom != null && bookingRoom.getId() == 0 ) {
//
//            Room savedRoom = roomRepository.save(bookingRoom);
//            theBooking.setRoom(savedRoom);
//        }
//        if (bookingUser != null && bookingUser.getId() == null) {
//
//            User savedUser = userRepository.save(bookingUser);
//            theBooking.setUser(savedUser);
//        }
//        return bookingRepository.save(theBooking);

}
