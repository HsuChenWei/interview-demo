package com.interview.demo.service.impl;


import com.interview.demo.entity.Booking;
import com.interview.demo.entity.QBooking;
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

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override//完成
    public List<Booking> findAllBooking() {
        return bookingRepository.findAll();
    }


    @Override
    public Option<Booking> findByUserId(String userId) {
//        return bookingRepository.findByUserId(userId);
        QBooking res = QBooking.booking;
        return Option.of(queryCtx.newQuery()
                .selectFrom(res)
                .where(res.userId.eq(userId))
                .fetchOne());
    }


//    @Override
//    public List<Booking> findAllBookingByUserId(String userId) {
//        return bookingRepository.findAllBookingByUserId(userId);
//    }


//    @Override
//    public Optional<Booking> findByBookingId(String id) {
//        Optional<Booking> optionalBooking = bookingRepository.findByBookingId(bookingId);
//
//        if (!optionalBooking.isPresent()) {
//            throw new RuntimeException("Can't find BookingId: " + bookingId);
//        }
//        return optionalBooking;
//    }


//    @Override
//    public void deleteByUserId(String bookingId) {
//        bookingRepository.deleteById(bookingId);
//    }



//    @Override
//    public Booking updateBookingDetail(String bookingId, Booking theBooking) {
//        Optional<Booking> optionalBooking = findByBookingId(bookingId);
//
//        if (optionalBooking.isPresent()) {
//            Booking dbBooking = optionalBooking.get();
//
//            dbBooking.setRoomId(theBooking.getRoomId());
//            dbBooking.setRoom(theBooking.getRoom());
//            dbBooking.setEndTime(theBooking.getEndTime());
//            dbBooking.setStartTime(theBooking.getStartTime());
//
//            return saveBooking(dbBooking);
//        } else {
//            throw new RuntimeException("Can't find BookingId: " + bookingId);
//        }
//    }

//    private Booking saveBooking(Booking booking) {
//        return bookingRepository.save(booking);
//    }



    //預定完會議室後，會員會被重複註冊(需修改)
//    @Override
//    public Booking save(Booking theBooking) {
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
//    }

//    @Override
//    public Optional<List<Booking>> searchAllBooking(String userId, SearchBooking body) {
//        return bookingRepository.findByUserId(userId);
//    }


}
