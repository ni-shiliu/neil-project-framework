package com.neil.project.api;

import cn.hutool.json.JSONUtil;
import com.neil.project.user.api.UserFacade;
import com.neil.project.user.dto.UserDTO;
import com.neil.project.user.dto.UserSaveDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao
 * @date 2024/6/18
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserFacade {

    @Override
    public UserDTO getUserById(@RequestParam("userId") Long userId) {
        return null;
    }

    @Override
    public UserDTO registerUser(@RequestBody UserSaveDTO userSaveDTO) {
        log.info("registerUser success param: {}", JSONUtil.toJsonStr(userSaveDTO));
        return null;
    }

}
