package com.neil.project.gateway;

import com.neil.project.user.dto.UserDTO;

/**
 * @author nihao
 * @date 2024/5/8
 */
public interface UserGateway {
    UserDTO getUserById(Long userId);
}
