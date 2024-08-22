package com.neil.project.security;

import jakarta.annotation.Resource;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author nihao
 * @date 2024/8/21
 */
@Service
public class JwtUserDetailCacheService extends CachingUserDetailsService {

    @Resource
    private JwtUserDetailsService userDetailsService;

    private static final String AUTH_USER_PREFIX = "AUTH_USER_";

    public JwtUserDetailCacheService(JwtUserDetailsService delegate) {
        super(delegate);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails user = super.getUserCache().getUserFromCache(AUTH_USER_PREFIX + username);
        if (Objects.isNull(user)) {
            user = userDetailsService.loadUserByUsername(username);
            Assert.notNull(user, () -> "UserDetailsService returned null for username " + username + ". This is an interface contract violation");
            super.getUserCache().putUserInCache(user);
        }
        return user;
    }
}
