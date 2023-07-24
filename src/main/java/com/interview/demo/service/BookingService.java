package com.interview.demo.service;

import com.interview.demo.entity.Booking;
import com.interview.demo.model.Booking.BookingDto;
import com.interview.demo.model.Booking.SearchBooking;
import io.vavr.control.Option;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    List<Booking> findAllBooking();//完成

//    List<Booking> findByUserId(String UserId);

    Option<Booking> findByUserId(String UserId);

//    Optional<Booking> findByBookingId(String bookingId);
//
//    List<Booking> findAllBookingByUserId(String userId);
//
//    void deleteByUserId(String bookingId);
//
//    Booking updateBookingDetail(String bookingId, Booking theBooking);
//
//    Booking save(Booking theBooking);



//    Optional<Booking> findBookingById(String id);
//
//    void deleteBookingById(String id);
//
//    Booking updateBooking(String id, BookingUpdates updates);
//
//    Booking save(BookingCreation creation);


}
