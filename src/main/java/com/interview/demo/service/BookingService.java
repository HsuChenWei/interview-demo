package com.interview.demo.service;

import com.interview.demo.entity.Booking;
import com.interview.demo.model.Booking.SearchBooking;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    List<Booking> findAllBooking();

    Optional<Booking> findByUserId(String UserId);

    Optional<Booking> findByBookingId(String bookingId);

    List<Booking> findAllBookingByUserId(String userId);

    void deleteByUserId(String bookingId);

    Booking updateBookingDetail(String bookingId, Booking theBooking);

    Booking save(Booking theBooking);

//    Optional<List<Booking>> searchAllBooking(String userId, SearchBooking body);




}
