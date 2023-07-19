package com.interview.demo.model.Room;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class RoomDto {
    @Schema(description = "會議室 ID")
    private int id;

    @Schema(description = "會議室名稱")
    private String roomName;

}
