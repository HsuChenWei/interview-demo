package com.interview.demo.model.Booking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class BookingUpdates {

    @Schema(description = "會議室 ID")
    private int roomId;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "開始時間")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "結束時間")
    private LocalDateTime endTime;
}
