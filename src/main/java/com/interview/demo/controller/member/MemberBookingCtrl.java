package com.interview.demo.controller.member;


import com.interview.demo.entity.Booking;
import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.model.Booking.BookingCreation;
import com.interview.demo.model.Booking.BookingDto;
import com.interview.demo.model.Booking.BookingUpdates;
import com.interview.demo.model.wrapper.RespWrapper;
import com.interview.demo.repository.RoomRepository;
import com.interview.demo.service.BookingService;
import com.interview.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Option;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/member/booking")
@Tag(name = "Member - Booking", description = "訂單功能")
public class MemberBookingCtrl {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoomRepository roomRepository;


    @Autowired
    private UserService userService;

    //    登入後列出個人的訂單(完成)
    @Operation(summary = "列出個人訂單")
    @GetMapping("/user")
    public RespWrapper<List<BookingDto>> getOwnBookings(Authentication authentication) {
        Option<List<Booking>> bookingsOption = bookingService.getOwnBookingByUserId(authentication);
        if (bookingsOption.isDefined()) {
            //取得當下會員資料庫訂單
            List<Booking> bookings = bookingsOption.get();
            //資料庫輸出轉成BookingDto欄位輸出前台
            List<BookingDto> bookingDtos = bookings.stream()
                    .map(booking -> modelMapper.map(booking, BookingDto.class))
                    .collect(Collectors.toList());
            return RespWrapper.success(bookingDtos);
        } else {
            throw new BadRequestException(ApiErrorCode.BOOKING_NOT_FOUND);
        }
    }

    //查詢單一比預定訂單(完成)
    @Operation(summary = "單一訂單查詢")
    @GetMapping("/{id}")
    public RespWrapper<BookingDto> getByBookingId(@PathVariable @Parameter(description = "設定訂單ID", required = true) String id) {
        return bookingService.getBookingById(id)
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new BadRequestException(ApiErrorCode.BOOKING_NOT_FOUND));
    }

    //取消會議室訂單(完成)
    @Operation(summary = "取消會議室訂單")
    @DeleteMapping("/delete/{id}")
    public RespWrapper<Void> deleteBooking(@PathVariable @Parameter(description = "設定訂單ID", required = true) String id) {
        bookingService.removeBookingById(id);
        return RespWrapper.success(null);
    }

    //依指定BookingId更新會議室預定欄位(完成)
    @Operation(summary = "更改會議室")
    @PutMapping("/update/{id}")
    public RespWrapper<BookingDto> updateBooking(@PathVariable @Parameter(description = "設定訂單ID", required = true) String id, @Validated @RequestBody BookingUpdates body) {
        return bookingService.updateBooking(id, body)
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new BadRequestException(ApiErrorCode.BOOKING_NOT_FOUND));
    }

    //預定會議室(完成)
    @Operation(summary = "預定會議室")
    @PostMapping
    public RespWrapper<BookingDto> createBooking(@RequestBody BookingCreation body) throws NotFoundException {
        return bookingService.createBooking(body)
                .flatMap(f -> bookingService.getBookingById(f.getId()))
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .map(RespWrapper::success)
                .get();
    }

    @GetMapping("/{roomId}/available-hourly-time-slots")
    public ResponseEntity<List<Map<String, String>>> getAvailableHourlyTimeSlots(
            @PathVariable int roomId) {
        // 调用 BookingService 中的方法来获取可用时间段
        Option<List<Map<String, String>>> optionalTimeSlots = bookingService.getAvailableTimeSlots(roomId);

        if (optionalTimeSlots.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, String>> availableTimeSlots = optionalTimeSlots.get();

        return ResponseEntity.ok(availableTimeSlots);
    }

}
