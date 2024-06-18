package com.neil.project.user.api;

import com.neil.myth.annotation.Myth;
import com.neil.project.user.dto.UserDTO;
import com.neil.project.user.dto.UserSaveDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author nihao
 * @date 2024/5/8
 */
@FeignClient(
        name = "neil-project-user",
        contextId = "neil-project-user",
        url = "localhost:8091"
)
@RequestMapping("/v1/user/")
public interface UserFacade {

    @GetMapping("getUserById")
    UserDTO getUserById(@RequestParam("userId") Long userId);

    @ApiOperation("注册用户")
    @PostMapping("registerUser")
    @Myth(destination = "GID-TOPIC1", target = UserFacade.class, targetMethod = "registerUser")
    UserDTO registerUser(@RequestBody UserSaveDTO userSaveDTO);

}
