package com.interview.demo.repository;

import com.interview.demo.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    Page<Booking> findAll(Specification<Booking> spec, Pageable pageable);

//    List<Booking> findByRoomIdAndStartTimeAfter(int id, LocalDateTime atStartOfDay);

    List<Booking> findByRoomIdAndStartTimeAfter(int id, Date todayStartTimestamp);


//    @Query(value = "", nativeQuery = true)
//    Page<Booking> findBookings(Pageable pageable,@Param("roomId") String roomId);
}
