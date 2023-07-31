package com.interview.demo.repository;

import com.interview.demo.entity.Booking;
import com.interview.demo.model.Booking.BookingDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface BookingRepository extends JpaRepository<Booking, String> {


}
