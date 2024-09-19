package com.neil.project.security;

import com.neil.project.exception.BizException;
import com.neil.project.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

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
        Jwt jwt = this.decode(token);
        if (Objects.isNull(jwt)) {
            return null;
        }
        Object userId = jwt.getId();
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }

    public String getUsername(String token) {
        Jwt jwt = this.decode(token);
        if (Objects.isNull(jwt)) {
            return null;
        }
        return jwt.getClaim("username");
    }

    private Jwt decode(String token) {
        Jwt jwt;
        try {
            jwt = jwtDecoder.decode(token);
        } catch (Exception e) {
            throw new BizException(ErrorCode.INVALID_TOKEN);
        }
        return jwt;
    }

}
