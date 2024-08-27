package com.neil.project.security.provider;

import com.neil.project.security.JwtUserDetails;
import com.neil.project.security.token.JwtApplicationToken;
import com.neil.project.security.token.MobilePasswordToken;
import com.neil.project.user.api.UserFacade;
import com.neil.project.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author nihao
 * @date 2024/8/21
 */
@Component
@RequiredArgsConstructor
public class MobilePasswordProvider implements AuthenticationProvider {

    private final UserFacade userFacade;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobilePasswordToken mobilePasswordToken = (MobilePasswordToken) authentication;
        UserDTO userDTO = userFacade.login(mobilePasswordToken.getMobilePasswordLoginDTO());

        if (Objects.nonNull(userDTO)) {
            JwtUserDetails userDetails = JwtUserDetails.builder()
                    .userId(userDTO.getId())
                    .mobile(userDTO.getMobile())
                    .username(userDTO.getUsername())
                    .build();
            return new JwtApplicationToken(userDetails);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobilePasswordToken.class.isAssignableFrom(authentication);
    }

}
