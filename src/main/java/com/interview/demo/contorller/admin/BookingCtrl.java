package com.interview.demo.contorller.admin;


import com.interview.demo.entity.Booking;
import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.model.Booking.BookingDto;
import com.interview.demo.model.wrapper.RespWrapper;
import com.interview.demo.service.BookingService;
import com.interview.demo.service.RoomService;
import com.interview.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


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

    @Autowired
    private ModelMapper modelMapper;


    //查詢所有會議室訂單(完成)
    @Operation(summary = "查詢所有會議室訂單")
    @GetMapping
    public RespWrapper<List<BookingDto>> findAllBooking(){
        return RespWrapper.success(bookingService.findAllBooking()
                .stream()
                .map(b -> modelMapper.map(b, BookingDto.class))
                .collect(Collectors.toList()));
    }

    //查詢用戶個人所有會議室訂單
//    @Operation(summary = "個人訂單查詢")
//    @GetMapping("/{userId}")
//    public RespWrapper<List<BookingDto>> findBookingById(@PathVariable String userId) {
//        return bookingService.findByUserId(userId)
//                .map(booking -> modelMapper.map(booking, BookingDto.class))
//                .map(RespWrapper::success)
//                .getOrElseThrow(() -> new BadRequestException(ApiErrorCode.POLICY_NOT_FOUND));
//    }




//    //取消會議室訂單(CORSFilter/WebSecurityConfiguration權限)
//    @Operation(summary = "取消會議室訂單")
//    @DeleteMapping("/{bookingId}")
//    public void deleteBookingDetail(@PathVariable String bookingId){
//        bookingService.deleteByUserId(bookingId);
//    }

    //預定會議室
//    @Operation(summary = "預定會議室")
//    @PostMapping
//    public Booking addBooking(@RequestBody Booking theBooking){
//
//        Booking dbBooking = bookingService.save(theBooking);
//        return dbBooking;

        //時間區間
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
//
//
//    }

//    //依指定BookingId更改會議室預定欄位
//    @Operation(summary = "更改會議室")
//    @PutMapping("/{bookingId}")
//    public Booking updateBookingDetail(@PathVariable String bookingId, @RequestBody Booking theBooking) {
//        Booking updateBooking = bookingService.updateBookingDetail(bookingId, theBooking);
//        return updateBooking;
//    }


//    private boolean isTimeOverlapping(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
//        return startTime1.isBefore(endTime2) && endTime1.isAfter(startTime2);
//    }
}
