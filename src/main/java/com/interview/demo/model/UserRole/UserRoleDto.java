package com.interview.demo.model.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class UserRoleDto {

//    @Schema(description = "使用者ID")
//    private String id;

    @Schema(description = "角色")
    private String userType;

}
