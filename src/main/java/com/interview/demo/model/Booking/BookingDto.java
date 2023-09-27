package com.interview.demo.model.Booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    @Schema(description = "訂單 ID")
    private String id;

    @Schema(description = "使用者 ID")
    private String userId;

    @Schema(description = "會議室 ID")
    private int roomId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "開始時間")
    private LocalDateTime startTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "結束時間")
    private LocalDateTime endTime;
}
