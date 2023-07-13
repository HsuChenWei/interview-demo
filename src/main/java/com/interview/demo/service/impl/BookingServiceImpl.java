package com.interview.demo.service.impl;

import com.interview.demo.repository.BookingRepository;
import com.interview.demo.entity.Booking;
import com.interview.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {


    @Autowired
    public BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
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
        return bookingRepository.findByBookingId(bookingId);
    }

    @Override
    public void deleteByUserId(String bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public Booking save(Booking theBooking) {
        return bookingRepository.save(theBooking);
    }





}
