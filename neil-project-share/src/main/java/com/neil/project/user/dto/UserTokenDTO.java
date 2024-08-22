package com.neil.project.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author nihao
 * @date 2024/8/21
 */
@Data
@Accessors(chain = true)
public class UserTokenDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -511274111188258246L;

    private Long userId;

    private String token;
}
