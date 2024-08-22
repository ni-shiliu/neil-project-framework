package com.neil.project.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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
public class MobilePasswordLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8742027566491432244L;

    @Schema(description = "手机号")
    @NotEmpty
    private String mobile;

    @Schema(description = "密码")
    @NotEmpty
    private String password;
}
