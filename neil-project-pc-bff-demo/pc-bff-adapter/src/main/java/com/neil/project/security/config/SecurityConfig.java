package com.neil.project.security.config;

import com.neil.project.security.JwtAuthenticationFilter;
import com.neil.project.security.JwtComponent;
import com.neil.project.security.JwtUserCache;
import com.neil.project.security.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author nihao
 * @date 2024/8/21
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUserCache userCache;

    private final JwtComponent jwtComponent;

    private final CachingUserDetailsService userDetailsService;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                        //swagger相关
                        "/doc.html",
                        "/swagger-resources",
                        "/webjars/**",
                        "/favicon.ico",
                        "/v2/api-docs",
                        "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers(
                        // 系统内部无需登陆接口统一配置
                        // 登陆
                        "/v1/pc/user/login"
                ).permitAll()
                .anyRequest().authenticated()
        );

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(exception -> exception.authenticationEntryPoint(restAuthenticationEntryPoint));

        http.csrf(AbstractHttpConfigurer::disable);

        userDetailsService.setUserCache(userCache);
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(userDetailsService, jwtComponent);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}