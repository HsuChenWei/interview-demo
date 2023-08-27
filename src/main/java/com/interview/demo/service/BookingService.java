package com.interview.demo.service;

import com.interview.demo.entity.Booking;
import com.interview.demo.model.Booking.BookingCreation;
import com.interview.demo.model.Booking.BookingDto;
import com.interview.demo.model.Booking.BookingUpdates;
import io.vavr.control.Option;
import javassist.NotFoundException;

import java.util.List;

public interface BookingService {

    List<Booking> findAllBooking();//查詢所有預定會議室訂單(完成)

    Option<List<Booking>> getByUserId(String userId);//查詢個人所有會議室訂單(完成)

    Option<Booking> getBookingById(String id);//查詢單一會議室訂單(完成)

    void removeBookingById(String id);//取消會議室訂單(完成)

    Option<Booking> updateBooking(String id, BookingUpdates updates);//會議室訂單更新(完成)

    Option<Booking> createBooking(BookingCreation creation) throws NotFoundException;//會議室預定系統(未完成)

    Option<Booking> getRoomId(String roomId);

    List<Booking> findFilteredBookings(int page, int size, String roomId, String userId, String startTime, String endTime);
}
