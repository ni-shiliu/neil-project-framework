package com.neil.project.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author nihao
 * @date 2024/10/10
 */
@Data
public class AiChatDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4817112048554258186L;

    @Schema(description = "应用NO")
    @NotEmpty
    private String applicationNo;

    @Schema(description = "用户ID")
    @NotNull
    private Long userId;

    @Schema(description = "用户输入")
    @NotEmpty
    private String input;
}
