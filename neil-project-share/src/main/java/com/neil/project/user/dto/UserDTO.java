package com.neil.project.user.dto;

import com.neil.project.common.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author nihao
 * @date 2024/5/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends BaseDTO {

    private String userName;
}
