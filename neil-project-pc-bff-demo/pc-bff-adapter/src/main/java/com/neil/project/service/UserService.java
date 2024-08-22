package com.neil.project.service;

import com.neil.project.user.dto.MobilePasswordLoginDTO;
import com.neil.project.user.dto.UserTokenDTO;

/**
 * @author nihao
 * @date 2024/8/21
 */
public interface UserService {

    UserTokenDTO login(MobilePasswordLoginDTO mobilePasswordLoginDTO);

}
