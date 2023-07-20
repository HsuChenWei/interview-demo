package com.interview.demo.service;

import com.interview.demo.entity.Booking;
import com.interview.demo.model.Booking.BookingCreation;
import com.interview.demo.model.Booking.BookingUpdates;
import com.interview.demo.model.Booking.SearchBooking;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    List<Booking> findAllBooking();

    List<Booking> findByUserId(String UserId);

    Optional<Booking> findBookingById(String id);

    void deleteBookingById(String id);

    Booking updateBooking(String id, BookingUpdates updates);

    Booking save(BookingCreation creation);

}
