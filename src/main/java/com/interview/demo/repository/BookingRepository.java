package com.interview.demo.repository;

import com.interview.demo.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface BookingRepository extends JpaRepository<Booking, String> {

    //    @Query("SELECT b FROM Booking b WHERE b.userId = :user_id")
    //    Optional<Booking> findByUserId(@Param("user_id") String userId);
    List<Booking> findByUserId(String userId);

    @Query("SELECT b FROM Booking b WHERE b.userId = :user_id")
    List<Booking> findAllBookingByUserId(@Param("user_id") String userId);


    @Query("SELECT b FROM Booking b WHERE b.id = :booking_id")
    Optional<Booking> findByBookingId(@Param("booking_id") String id);


}
