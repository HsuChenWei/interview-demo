package com.interview.demo.model.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserLogin {

    @Schema(description = "名稱")
    private String userName;

    @Schema(description = "密碼")
    private String userPwd;
}
