package com.interview.demo.security;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.interview.demo.service.UserService;
import com.interview.demo.service.impl.jwt.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        if (authToken != null && authToken.startsWith(BEARER_PREFIX)) {
            try {
                authToken = authToken.substring(BEARER_PREFIX.length()).trim();
                DecodedJWT jwt = jwtHelper.verify(authToken);
                String userId = jwt.getClaim("sid").asString();
                String userType = jwt.getClaim("userType").asString();

                //時間轉換
                Date accessExpiredAtDate = jwt.getExpiresAt();
                Instant accessExpiredAtInstant = accessExpiredAtDate.toInstant();
                OffsetDateTime accessExpiredAt = OffsetDateTime.ofInstant(accessExpiredAtInstant, ZoneOffset.UTC);

                jwt.getExpiresAt();

                UserPasswordAuthenticationToken authentication = userService.getUserById(userId)
                        .filter(s -> OffsetDateTime.now().isBefore(accessExpiredAt))
                        .map(s -> {
                            List<GrantedAuthority> authorities = new ArrayList<>();
                            if (Objects.equals(userType, "0")) {
                                authorities.add(new SimpleGrantedAuthority("USER"));
                            } else if (Objects.equals(userType, "1")) {
                                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                            }
                            return new UserPasswordAuthenticationToken(s.getId(), null , authorities);

                        })
                        .getOrElse(() -> null);

                if (authentication != null) {
                    authentication.setDetails(userId);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTVerificationException | UsernameNotFoundException ex) {
                log.warn("Invalid authorization token: " + ex.getMessage());
            }
        }
        chain.doFilter(request, response);
    }
}
