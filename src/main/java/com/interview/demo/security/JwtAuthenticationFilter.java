package com.interview.demo.security;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.interview.demo.service.UserService;
import com.interview.demo.service.impl.jwt.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;


    private final String BEARER_PREFIX = "Bearer";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(this.tokenHeader);
        if (authToken != null && !authToken.isEmpty() && authToken.startsWith(BEARER_PREFIX)) {
            try {
                authToken = authToken.substring(BEARER_PREFIX.length()).trim();
                DecodedJWT jwt = jwtHelper.verify(authToken);
                String userId = jwt.getClaim("sid").asString();

                Date accessExpiredAtDate = jwt.getExpiresAt(); // 获取过期时间的Date对象
                Instant accessExpiredAtInstant = accessExpiredAtDate.toInstant(); // 转换为Instant
                OffsetDateTime accessExpiredAt = OffsetDateTime.ofInstant(accessExpiredAtInstant, ZoneOffset.UTC); // 转换为 OffsetDateTime
                LocalDateTime localAccessExpiredAt = accessExpiredAt.toLocalDateTime(); // 转换为 LocalDateTime

                jwt.getExpiresAt();
                userService.getUserById(userId)
                        .filter(s -> OffsetDateTime.now().isBefore(accessExpiredAt))
                        .map(s -> {
                            UserPasswordAuthenticationToken authentication = new UserPasswordAuthenticationToken(s.getId(), null, Collections.emptyList());
                            authentication.setDetails(userId);
                            return (Authentication) authentication;

                        })
//                        .flatMap(s -> {
////                            if (s.getUserId() != null) {
//                                return userService.getById(jwt.getSubject())
//                                        .map(User::getId)
//                                        .map(userId -> {
//                                            UserPasswordAuthenticationToken authentication = new UserPasswordAuthenticationToken(s.getUserId(), null, Collections.emptyList());
//                                            authentication.setDetails(sessionId);
//                                            return (Authentication) authentication;
//                                        });
////                            } else if (s.getMemberId() != null) {
////                                return memberService.getMemberById(jwt.getSubject())
////                                        .map(Member::getId)
////                                        .map(memberId -> {
////                                            MemberPasswordAuthenticationToken authentication = new MemberPasswordAuthenticationToken(s.getMemberId(), null, Collections.emptyList());
////                                            authentication.setDetails(sessionId);
////                                            return (Authentication) authentication;
////                                        });
////                            }
//                            return Option.none();
//                        })
                        .forEach(authentication -> {
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        });
            } catch (JWTVerificationException | UsernameNotFoundException ex) {
                log.warn("Invalid authorization token: " + ex.getMessage());
            }
        }
        chain.doFilter(request, response);
    }

}
