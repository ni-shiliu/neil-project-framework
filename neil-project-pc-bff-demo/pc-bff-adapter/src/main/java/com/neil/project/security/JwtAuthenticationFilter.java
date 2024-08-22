package com.neil.project.security;

import com.neil.project.security.token.JwtApplicationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * @author nihao
 * @date 2024/8/21
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CachingUserDetailsService userDetailsService;

    private final JwtComponent jwtComponent;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> optionalJwt = parseJwt(request);

            if (optionalJwt.isPresent()) {
                String jwtToken = optionalJwt.get();

                Long userId = jwtComponent.getUserId(jwtToken);
                if (Objects.isNull(userId)) {
                    return;
                }
                JwtUserDetails jwtUserDetails = (JwtUserDetails) userDetailsService.loadUserByUsername(userId.toString());
                //到这里没抛异常，说明认证已过
                JwtApplicationToken authentication =
                        new JwtApplicationToken(jwtUserDetails.getAuthorities(), jwtUserDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        String jwtToken = null;
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            jwtToken = headerAuth.substring(7);
        }

        return Optional.ofNullable(jwtToken);
    }
}
