package com.neil.project.user.api;

import com.neil.myth.annotation.Myth;
import com.neil.project.user.dto.UserDTO;
import com.neil.project.user.dto.UserSaveDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author nihao
 * @date 2024/5/8
 */
@Tag(name = "用户服务")
@FeignClient(
        name = "neil-project-user",
        contextId = "neil-project-user",
        url = "localhost:8091"
)
public interface UserFacade {

    @RequestMapping(method = RequestMethod.GET, value = "/v1/user/getUserById")
    UserDTO getUserById(@RequestParam("userId") Long userId);

    @Operation(summary = "注册用户")
    @RequestMapping(value = "/v1/user/registerUser", method = RequestMethod.POST)
    @Myth(destination = "MYTH-USER")
    UserDTO registerUser(@RequestBody UserSaveDTO userSaveDTO);

}
