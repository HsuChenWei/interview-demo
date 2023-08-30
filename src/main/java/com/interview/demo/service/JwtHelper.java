//package com.interview.demo.service;
//
//import com.auth0.jwt.interfaces.DecodedJWT;
//
//import java.time.OffsetDateTime;
//import java.util.Map;
//
///**
// * 封裝 JWT 生成、驗證等相關操作方法
// */
//public interface JwtHelper {
//
//    /**
//     * 驗證 JWT 字串是否有效
//     * @param token JWT 字串
//     * @return 當JWT 字串有效時，回傳解碼後的 JWT Payload 內容。
//     * @throws com.auth0.jwt.exceptions.JWTVerificationException 當JWT 字串無效時擲出。
//     */
//    DecodedJWT verify(String token);
//    /**
//     * 解碼 JWT 字串內容。請注意此方法僅做內容解碼，並不會驗證 JWT 的有效性 (如效期、簽章是否吻合)，因此不可用來驗證身份。驗證身份請使用 JwtHelper.verifyToken。
//     * @param token JWT 字串
//     * @return 回傳解碼後的 JWT Payload 內容。
//     * @throws com.auth0.jwt.exceptions.JWTDecodeException 當 JWT 無法解碼時擲出。
//     */
//    DecodedJWT decode(String token);
//
//    String sign(String sub, OffsetDateTime exp, Map<String, String> claims);
//}