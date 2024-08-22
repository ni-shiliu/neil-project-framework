package com.neil.project.security.token;

import com.neil.project.security.JwtUserDetails;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author nihao
 * @date 2024/8/21
 */
@Getter
public class JwtApplicationToken extends AbstractAuthenticationToken implements Serializable {

    @Serial
    private static final long serialVersionUID = 6594547209599330810L;

    private final JwtUserDetails jwtUserDetails;

    public JwtApplicationToken(Collection<? extends GrantedAuthority> authorities, JwtUserDetails jwtUserDetails) {
        super(authorities);
        this.jwtUserDetails = jwtUserDetails;
        this.setAuthenticated(true);
    }

    public JwtApplicationToken(JwtUserDetails jwtUserDetails) {
        super(null);
        this.jwtUserDetails = jwtUserDetails;
        this.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return jwtUserDetails;
    }

}
