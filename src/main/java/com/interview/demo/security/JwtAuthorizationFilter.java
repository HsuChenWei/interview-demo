//package com.interview.demo.security;
//
//import io.jsonwebtoken.Claims;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
//    private final JwtUtil jwtUtil;
//
//    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
//        super(authenticationManager);
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain chain) throws IOException, ServletException {
//        Authentication authentication = getAuthentication(request);
//        if (authentication != null) {
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        chain.doFilter(request, response);
//    }
//
//    private Authentication getAuthentication(HttpServletRequest request) {
//        String token = jwtUtil.extractToken(request);
//        if (token != null && jwtUtil.validateToken(token)) {
//            Claims claims = jwtUtil.extractClaims(token);
//            String username = claims.getSubject();
//            if (username != null) {
//                return new UsernamePasswordAuthenticationToken(username, null, null);
//            }
//        }
//        return null;
//    }
//}
