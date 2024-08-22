package com.neil.project.security;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;

/**
 * @author nihao
 * @date 2024/8/21
 */
@Data
@Builder
public class JwtUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = -7562250101440690319L;

    private Long userId;

    private String mobile;

    private String username;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

}
