package com.interview.demo.repository;

import com.interview.demo.entity.Booking;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    Page<Booking> findAll(Specification<Booking> spec, Pageable pageable);

    List<Booking> findByRoomIdAndStartTimeAfter(int id, LocalDateTime atStartOfDay);


//    @Query(value = "", nativeQuery = true)
//    Page<Booking> findBookings(Pageable pageable,@Param("roomId") String roomId);
}
