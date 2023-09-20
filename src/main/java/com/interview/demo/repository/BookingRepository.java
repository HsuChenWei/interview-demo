package com.interview.demo.repository;

import com.interview.demo.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    Page<Booking> findAll(Specification<Booking> spec, Pageable pageable);

    List<Booking> findByRoomIdAndStartTimeAfter(int id, Date todayStartTimestamp);

    @Query("SELECT b FROM Booking b WHERE b.roomId = :roomId AND b.startTime BETWEEN :startDateTime AND :endDateTime")
    List<Booking> findBookingsByRoomIdAndTimeRange(
            @Param("roomId") int roomId,
            @Param("startDateTime") Timestamp startDateTime,
            @Param("endDateTime") Timestamp endDateTime
    );
}
