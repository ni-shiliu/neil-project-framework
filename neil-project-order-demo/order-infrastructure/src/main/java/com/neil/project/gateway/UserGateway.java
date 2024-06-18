package com.neil.project.gateway;

import com.neil.project.user.dto.UserDTO;
import com.neil.project.user.dto.UserSaveDTO;

/**
 * @author nihao
 * @date 2024/5/8
 */
public interface UserGateway {
    UserDTO getUserById(Long userId);

    void registerUser(UserSaveDTO userSaveDTO);

}
