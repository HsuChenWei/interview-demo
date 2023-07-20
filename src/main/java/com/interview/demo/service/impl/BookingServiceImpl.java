package com.interview.demo.service.impl;

import com.interview.demo.entity.Room;
import com.interview.demo.entity.User;
import com.interview.demo.model.Booking.SearchBooking;
import com.interview.demo.repository.BookingRepository;
import com.interview.demo.entity.Booking;
import com.interview.demo.repository.RoomRepository;
import com.interview.demo.repository.UserRepository;
import com.interview.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {


    @Autowired
    public BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              RoomRepository roomRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<Booking> findAllBooking() {
        return bookingRepository.findAll();
    }


    @Override
    public Optional<Booking> findByUserId(String userId) {
        return bookingRepository.findByUserId(userId);
    }


    @Override
    public List<Booking> findAllBookingByUserId(String userId) {
        return bookingRepository.findAllBookingByUserId(userId);
    }


    @Override
    public Optional<Booking> findByBookingId(String bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findByBookingId(bookingId);

        if (!optionalBooking.isPresent()) {
            throw new RuntimeException("Can't find BookingId: " + bookingId);
        }
        return optionalBooking;
    }


    @Override
    public void deleteByUserId(String bookingId) {
        bookingRepository.deleteById(bookingId);
    }



    @Override
    public Booking updateBookingDetail(String bookingId, Booking theBooking) {
        Optional<Booking> optionalBooking = findByBookingId(bookingId);

        if (optionalBooking.isPresent()) {
            Booking dbBooking = optionalBooking.get();

            dbBooking.setRoomId(theBooking.getRoomId());
            dbBooking.setRoom(theBooking.getRoom());
            dbBooking.setEndTime(theBooking.getEndTime());
            dbBooking.setStartTime(theBooking.getStartTime());

            return saveBooking(dbBooking);
        } else {
            throw new RuntimeException("Can't find BookingId: " + bookingId);
        }
    }
    private Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }



    //預定完會議室後，會員會被重複註冊(需修改)
    @Override
    public Booking save(Booking theBooking) {
        Room bookingRoom = theBooking.getRoom();
        User bookingUser = theBooking.getUser();

        if (bookingRoom != null && bookingUser != null) {

//            Room savedRoom = roomRepository.save(bookingRoom);
//            theBooking.setRoom(savedRoom);
            return bookingRepository.save(theBooking);
        }
//        if (bookingUser != null) {
//
//
//        }
//        return bookingRepository.save(theBooking);
    }

//    @Override
//    public Optional<List<Booking>> searchAllBooking(String userId, SearchBooking body) {
//        return bookingRepository.findByUserId(userId);
//    }


}
