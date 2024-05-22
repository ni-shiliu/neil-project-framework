package com.neil.project.user.api;

import com.neil.project.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
