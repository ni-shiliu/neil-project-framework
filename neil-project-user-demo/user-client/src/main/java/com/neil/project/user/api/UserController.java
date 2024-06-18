package com.neil.project.user.api;

import com.neil.project.user.dto.UserDTO;
import com.neil.project.user.dto.UserSaveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao
 * @date 2024/6/18
 */
@RestController
@RequiredArgsConstructor
public class UserController implements UserFacade {

    @Override
    public UserDTO getUserById(Long userId) {
        return null;
    }

    @Override
    public UserDTO registerUser(@RequestBody UserSaveDTO userSaveDTO) {
        return null;
    }

}
