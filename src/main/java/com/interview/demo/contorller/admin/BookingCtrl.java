package com.interview.demo.contorller.admin;


import com.interview.demo.entity.Booking;
import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.model.Booking.BookingCreation;
import com.interview.demo.model.Booking.BookingDto;
import com.interview.demo.model.Booking.BookingUpdates;
import com.interview.demo.model.wrapper.RespWrapper;
import com.interview.demo.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
    private ModelMapper modelMapper;

    //查詢所有會議室訂單(完成)
    @Operation(summary = "查詢所有會議室訂單")
    @GetMapping
    public RespWrapper<List<BookingDto>> findFilteredBookings(
            @RequestParam(defaultValue = "0") @Parameter(description = "分頁索引 (0-based)", required = true) int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "分頁大小", required = true) int size,
            @RequestParam(required = false) @Parameter(description = "設定訂單ID") String id,
            @RequestParam(required = false) @Parameter(description = "設定會議室ID") String roomId,
            @RequestParam(required = false) @Parameter(description = "設定使用者ID") String userId,
            @RequestParam(required = false) @Parameter(description = "設定訂單開始時間") String startTime,
            @RequestParam(required = false) @Parameter(description = "設定訂單結束時間") String endTime) {
        List<Booking> filteredBookings = bookingService.findFilteredBookings(page, size, roomId, userId, startTime, endTime, id);
        return RespWrapper.success(bookingService.findFilteredBookings(page, size, roomId, userId, startTime, endTime, id)
                .stream()
                .map(b -> modelMapper.map(b, BookingDto.class))
                .collect(Collectors.toList()));
    }

    //查詢用戶個人所有會議室訂單(完成)
    @Operation(summary = "個人訂單查詢")
    @GetMapping("/user/{userId}")
    public RespWrapper<List<BookingDto>> getByUserId(@PathVariable @Parameter(description = "設定使用者ID", required = true) String userId) {
        return bookingService.getByUserId(userId)
                .map(bookingList -> bookingList.stream()
                        .map(booking -> modelMapper.map(booking, BookingDto.class))
                        .collect(Collectors.toList()))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new BadRequestException(ApiErrorCode.BOOKING_NOT_FOUND));
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
    //(CORSFilter/WebSecurityConfiguration權限)
    @Operation(summary = "取消會議室訂單")
    @DeleteMapping("/{id}")
    public RespWrapper<Void> deleteBooking(@PathVariable @Parameter(description = "設定訂單ID", required = true) String id) {
        bookingService.removeBookingById(id);
        return RespWrapper.success(null);
    }

    //依指定BookingId更新會議室預定欄位(完成)
    @Operation(summary = "更改會議室")
    @PutMapping("/{id}")
    public RespWrapper<BookingDto> updateBooking(@PathVariable @Parameter(description = "設定訂單ID", required = true) String id, @Validated @RequestBody BookingUpdates body) {
        return bookingService.updateBooking(id, body)
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new BadRequestException(ApiErrorCode.BOOKING_NOT_FOUND));
    }

    //預定會議室(未完成)
    @Operation(summary = "預定會議室")
    @PostMapping("/{userId}")
    public RespWrapper<BookingDto> createBooking(@RequestBody BookingCreation body) throws NotFoundException {
        return bookingService.createBooking(body)
                .flatMap(f -> bookingService.getBookingById(f.getId()))
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .map(RespWrapper::success)
                .get();
    }

}
