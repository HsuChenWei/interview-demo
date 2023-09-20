package com.interview.demo.service;

import com.interview.demo.entity.Booking;
import com.interview.demo.model.Booking.BookingCreation;
import com.interview.demo.model.Booking.BookingUpdates;
import io.vavr.control.Option;
import javassist.NotFoundException;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface BookingService {

    List<Booking> findAllBooking();//查詢所有預定會議室訂單(完成)

    Option<List<Booking>> getByUserId(String userId);//查詢個人所有會議室訂單(完成)

    Option<List<Booking>> getOwnBookingByUserId(Authentication authentication);

    Option<Booking> getBookingById(String id);//查詢單一會議室訂單(完成)

    void removeBookingById(String id);//取消會議室訂單(完成)

    Option<Booking> updateBooking(String id, BookingUpdates updates);//會議室訂單更新(完成)

    Option<Booking> createBooking(BookingCreation creation) throws NotFoundException;//會議室預定系統(完成)

    Option<List<Map<String, String>>> getAvailableTimeSlots(int roomId);

    List<Booking> findFilteredBookings(int page, int size, String roomId, String userId, String startTime, String endTime, String id);
}
