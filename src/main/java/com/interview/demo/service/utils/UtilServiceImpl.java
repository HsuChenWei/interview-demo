package com.interview.demo.service.utils;


import com.interview.demo.model.Security.TokenPair;
import com.interview.demo.service.UserService;
import com.interview.demo.service.impl.jwt.JwtHelper;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UtilServiceImpl implements UtilService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public Option<TokenPair> generateTokenPair(String id) {
        return userService.getUserById(id)
                .map(s -> {

                    List<String> userRoles = userService.getRoleTypeByUserId(s.getId());
                    String userType = userRoles.get(0);

                    //創建當下的時間
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    //token有效時間(2hr)
                    LocalDateTime accessExpiredAt = currentDateTime.plusHours(2);

                    Map<String, String> accessClaims = new HashMap<>();
                    accessClaims.put("typ", "Bearer");
                    accessClaims.put("sid", s.getId());
                    accessClaims.put("userType", userType);
                    String accessToken = jwtHelper.sign(s.getId(), accessExpiredAt, accessClaims);

                    Map<String, String> refreshClaims = new HashMap<>();
                    refreshClaims.put("typ", "Refresh");
                    refreshClaims.put("sid", s.getId());
                    String refreshToken = jwtHelper.sign(s.getId(), accessExpiredAt, refreshClaims);

                    return TokenPair.builder()
                            .accessToken(accessToken)
                            .accessExpiryTime(accessExpiredAt)
                            .refreshToken(refreshToken)
                            .refreshExpiryTime(accessExpiredAt)
                            .build();
                });

    }

}
