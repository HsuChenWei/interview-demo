package com.interview.demo.model.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChangeUserRole {

    @Schema(description = "角色")
    private int userType;
}
