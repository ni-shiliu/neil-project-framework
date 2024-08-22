package com.neil.project.service.impl;

import com.neil.project.security.JwtComponent;
import com.neil.project.security.JwtUserDetails;
import com.neil.project.security.token.MobilePasswordToken;
import com.neil.project.service.UserService;
import com.neil.project.user.dto.MobilePasswordLoginDTO;
import com.neil.project.user.dto.UserTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @author nihao
 * @date 2024/8/21
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtComponent jwtComponent;

    private final AuthenticationManager authenticationManager;


    @Override
    public UserTokenDTO login(MobilePasswordLoginDTO mobilePasswordLoginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new MobilePasswordToken(mobilePasswordLoginDTO));

        String token = jwtComponent.generateToken(authentication);
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        return new UserTokenDTO()
                .setUserId(userDetails.getUserId())
                .setToken(token);
    }
}
