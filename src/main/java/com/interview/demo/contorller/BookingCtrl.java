package com.interview.demo.contorller;


import com.interview.demo.entity.Booking;
import com.interview.demo.entity.Room;
import com.interview.demo.entity.User;
import com.interview.demo.service.BookingService;
import com.interview.demo.service.RoomService;
import com.interview.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
@Tag(name = "Booking", description = "訂單功能")
public class BookingCtrl {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    //查詢用戶個人所有會議室訂單（完成）
    @Operation(summary = "個人訂單查詢")
    @GetMapping("/{userId}")
    public List<Booking> findBookingById(@PathVariable String userId) {
        List<Booking> theBooking = bookingService.findAllBookingByUserId(userId);
        if (theBooking == null) {
            throw new RuntimeException("Can't find the userId:" + userId);
        }
        return theBooking;
    }

    //查詢所有會議室訂單(完成)
    @Operation(summary = "查詢所有會議室訂單")
    @GetMapping
    public List<Booking> findAllBooking(){
        return bookingService.findAllBooking();
    }


    //取消會議室訂單(完成，CORSFilter/WebSecurityConfiguration權限)
    @Operation(summary = "取消會議室訂單")
    @DeleteMapping("/{bookingId}")
    public void deleteBookingDetail(@PathVariable String bookingId){
        bookingService.deleteByUserId(bookingId);
    }

    //預定會議室(完成)
    @Operation(summary = "預定會議室")
    @PostMapping
    public Booking addBooking(@RequestBody Booking theBooking){
        Room bookingRoom = theBooking.getRoom();
        User bookingUser = theBooking.getUser();

        if (bookingRoom != null && bookingRoom.getId() == 0 ) {

            Room savedRoom = roomService.save(bookingRoom);
            theBooking.setRoom(savedRoom);
        }
        if (bookingUser != null && bookingUser.getId() == null) {

            User savedUser = userService.save(bookingUser);
            theBooking.setUser(savedUser);
        }

//        MeetingRoomScheduler scheduler = new MeetingRoomScheduler();
//        List<TimeSlot> bookedTimeSlots = scheduler.getBookedTimeSlots();
//        // 检查预定时间是否与已预订的时间段重叠
//        Timestamp startTime = theBooking.getStartTime();
//        Timestamp endTime = theBooking.getEndTime();
//        LocalTime startTimeOnly = startTime.toLocalDateTime().toLocalTime();
//        LocalTime endTimeOnly = endTime.toLocalDateTime().toLocalTime();
//
//        LocalTime openTime = LocalTime.of(9, 0);
//        LocalTime closeTime = LocalTime.of(18, 0);
//
//        if (startTimeOnly.isBefore(openTime) || endTimeOnly.isAfter(closeTime)) {
//
//            throw new IllegalArgumentException("預定時間不在開放的時間時段");
//        }
//
//        boolean isOverlapping = false;
//        for (TimeSlot bookedSlot : bookedTimeSlots) {
//            LocalTime bookedStartTime = bookedSlot.getStartTime();
//            LocalTime bookedEndTime = bookedSlot.getEndTime();
//            if (isTimeOverlapping(startTimeOnly, endTimeOnly, bookedStartTime, bookedEndTime)) {
//                isOverlapping = true;
//                break;
//            }
//        }
//
//        if (isOverlapping) {
//            throw new IllegalArgumentException("預定的時間與已預訂得時間區間重疊");
//        }

        Booking dbBooking = bookingService.save(theBooking);
        return dbBooking;
    }

    //依指定BookingId更改會議室預定欄位(完成)
    @Operation(summary = "更改會議室")
    @PutMapping("/{bookingId}")
    public Booking updateBookingDetail(@PathVariable String bookingId, @RequestBody Booking theBooking) {
        Optional<Booking> optionalBooking = bookingService.findByBookingId(bookingId);

        if (optionalBooking.isPresent()) {
            Booking dbBooking = optionalBooking.get();

            dbBooking.setRoom(theBooking.getRoom());
            dbBooking.setEndTime(theBooking.getEndTime());
            dbBooking.setStartTime(theBooking.getStartTime());

            Booking savedBooking = bookingService.save(dbBooking);
            return savedBooking;
        } else {
            throw new RuntimeException("Can't found BookingId：" + bookingId);
        }

    }

//    private boolean isTimeOverlapping(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
//        return startTime1.isBefore(endTime2) && endTime1.isAfter(startTime2);
//    }
}
