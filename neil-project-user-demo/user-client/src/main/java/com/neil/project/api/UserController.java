package com.neil.project.api;

import com.neil.myth.annotation.Myth;
import com.neil.project.user.dto.UserDTO;
import com.neil.project.user.dto.UserSaveDTO;
import com.neil.project.user.api.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao
 * @date 2024/6/18
 */
@RestController
@RequiredArgsConstructor
public class UserController implements UserFacade {

    @Override
    public UserDTO getUserById(@RequestParam("userId") Long userId) {
        return null;
    }

    @Override
    @Myth(destination = "GID-TOPIC1")
    public UserDTO registerUser(@RequestBody UserSaveDTO userSaveDTO) {
        return null;
    }

}
