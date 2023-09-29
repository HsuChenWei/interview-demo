package com.interview.demo.model.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserUpdate {


    @Schema(description = "密碼")
    private String userPwd;


}