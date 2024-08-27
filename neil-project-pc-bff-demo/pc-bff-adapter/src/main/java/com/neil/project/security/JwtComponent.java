package com.neil.project.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @author nihao
 * @date 2024/8/21
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtComponent {

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    @Value("${jwt.expiration.seconds:36000}")
    private Long jwtExpirationSeconds;

    @Value("${sign.secret:a5a2d17f-e7ff-428e-b8e2-b862107ff1b3}")
    private String secret;


    public String generateToken(Authentication authentication) {
        JwtUserDetails userPrincipal = (JwtUserDetails) authentication.getPrincipal();
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtExpirationSeconds))
                .id(userPrincipal.getUserId().toString())
                .claim("mobile", userPrincipal.getMobile())
                .claim("username", userPrincipal.getUsername())
                .claim("secret", secret)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String getMobile(String token) {
        return jwtDecoder.decode(token).getClaim("mobile");
    }

    public Long getUserId(String token) {
        Object userId = jwtDecoder.decode(token).getId();
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }

    public String getUsername(String token) {
        return jwtDecoder.decode(token).getClaim("username");
    }

}
