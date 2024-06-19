package com.neil.project.gateway.impl;

import com.neil.project.user.api.UserFacade;
import com.neil.project.gateway.UserGateway;
import com.neil.project.user.dto.UserDTO;
import com.neil.project.user.dto.UserSaveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author nihao
 * @date 2024/5/8
 */
@Component
@RequiredArgsConstructor
public class UserGatewayImpl implements UserGateway {

    private final UserFacade userFacade;

    @Override
    public UserDTO getUserById(Long userId) {
        return userFacade.getUserById(userId);
    }

    @Override
    public void registerUser(UserSaveDTO userSaveDTO) {
        userFacade.registerUser(userSaveDTO);
    }
}
