package com.interview.demo.model.Booking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class SearchBooking {
    @Schema(description = "訂單 ID")
    private String id;

    @Schema(description = "使用者 ID")
    private String userId;

    @Schema(description = "會議室 ID")
    private int roomId;

    @Schema(description = "開始間")
    private Timestamp startTime;

    @Schema(description = "結束時間")
    private Timestamp endTime;
}
