package com.interview.demo.model.Booking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class BookingUpdates {

    @Schema(description = "會議室 ID")
    private int roomId;

    @Schema(description = "開始間")
    private Timestamp startTime;

    @Schema(description = "結束時間")
    private Timestamp endTime;
}
