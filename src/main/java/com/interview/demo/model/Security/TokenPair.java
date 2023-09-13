package com.interview.demo.model.Security;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TokenPair {
    @Schema(description = "Access Token (用於後續 Request 身份驗證)")
    private String accessToken;
    @Schema(description = "Refresh Token")
    private String refreshToken;
    @Schema(description = "Access Token 過期時間")
    private LocalDateTime accessExpiryTime;
    @Schema(description = "Refresh Token 過期時間")
    private LocalDateTime refreshExpiryTime;
}
