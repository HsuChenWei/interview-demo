package com.interview.demo.service.utils;


import com.interview.demo.model.TokenPair;
import com.interview.demo.service.UserService;
import com.interview.demo.service.impl.jwt.JwtHelper;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
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

                    LocalDateTime currentDateTime = LocalDateTime.now();
                    LocalDateTime accessExpiredAt = currentDateTime.plus(2, ChronoUnit.HOURS);

                    Map<String, String> accessClaims = new HashMap<>();
                    accessClaims.put("typ", "Bearer");
                    accessClaims.put("sid", s.getId());
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
