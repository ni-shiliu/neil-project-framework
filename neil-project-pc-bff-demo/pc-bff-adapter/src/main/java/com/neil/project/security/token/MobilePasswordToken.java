package com.neil.project.security.token;

import com.neil.project.user.dto.MobilePasswordLoginDTO;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import javax.security.auth.Subject;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author nihao
 * @date 2024/8/21
 */
public class MobilePasswordToken extends AbstractAuthenticationToken implements Serializable {

    @Serial
    private static final long serialVersionUID = 7199239905780979718L;

    private final MobilePasswordLoginDTO mobilePasswordLoginDTO;


    public MobilePasswordToken(MobilePasswordLoginDTO mobilePasswordLoginDTO) {
        super(null);
        this.mobilePasswordLoginDTO = mobilePasswordLoginDTO;
        this.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }

    public MobilePasswordLoginDTO getUserLoginDTO() {
        return mobilePasswordLoginDTO;
    }
}
