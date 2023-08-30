//package com.interview.demo.security;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.interfaces.DecodedJWT;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Date;
//
//public class JwtUtil {
//    private static final String SECRET_KEY = "your-secret-key";
//    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
//
//    public static String generateToken(String username) {
//        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
//        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
//
//        String token = JWT.create()
//                .withSubject(username)
//                .withExpiresAt(expirationDate)
//                .sign(algorithm);
//
//        return token;
//    }
//
//    public static String extractUsername(String token) {
//        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
//        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
//        return jwt.getSubject();
//    }
//
//    public String extractToken(HttpServletRequest request) {
//    }
//
//    public boolean validateToken(String token) {
//    }
//
//    public Claims extractClaims(String token) {
//    }
//
//    // 其他 JWT 相关方法
//}