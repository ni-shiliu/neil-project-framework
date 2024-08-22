package com.neil.project.adapter.controller;

import com.neil.project.common.BaseResult;
import com.neil.project.service.UserService;
import com.neil.project.user.dto.MobilePasswordLoginDTO;
import com.neil.project.user.dto.UserTokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author nihao
 * @date 2024/8/21
 */
@Tag(name = "用户模块")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pc/user/")
public class UserController {

    private final UserService userService;

    @Operation(summary = "登陆")
    @PostMapping("login")
    public BaseResult<UserTokenDTO> login(@RequestBody @Valid MobilePasswordLoginDTO mobilePasswordLoginDTO) {
        UserTokenDTO userTokenDTO = userService.login(mobilePasswordLoginDTO);
        return BaseResult.success(userTokenDTO);
    }

}
