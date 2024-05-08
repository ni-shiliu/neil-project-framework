package com.neil.project.user.api;

import com.neil.project.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author nihao
 * @date 2024/5/8
 */
@FeignClient(
        name = "neil-project-user",
        contextId = "neil-project-user",
        url = "${neil.project.user.url}"
)
public interface UserFacade {

    UserDTO getUserById(@RequestParam("userId") Long userId);

}
