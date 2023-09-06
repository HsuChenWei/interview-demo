package com.interview.demo.service.impl.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.interview.demo.service.impl.jwt.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class JwtHelperImpl implements JwtHelper {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.iss}")
    private String iss;

    @Value("${jwt.aud}")
    private String aud;

    @Override
    public DecodedJWT verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(iss)
                .build();
        return verifier.verify(token);

    }

    @Override
    public DecodedJWT decode(String token) {
        return JWT.decode(token);
    }

    @Override
    public String sign(String sub, LocalDateTime exp, Map<String, String> claims) {
        JWTCreator.Builder builder = JWT.create()
                .withIssuer(iss)
                .withAudience(aud)
                .withSubject(sub)
                .withExpiresAt(Date.from(exp.atZone(ZoneId.systemDefault()).toInstant()));
        if(claims != null){
            for (Map.Entry<String, String> entry : claims.entrySet()) {
                builder.withClaim(entry.getKey(), entry.getValue());
            }
        }
        return builder.sign(Algorithm.HMAC256(secret));
    }


}
