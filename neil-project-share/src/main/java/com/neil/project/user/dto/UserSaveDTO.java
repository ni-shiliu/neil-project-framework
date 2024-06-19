package com.neil.project.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author nihao
 * @date 2024/6/18
 */
@Data
@Accessors(chain = true)
public class UserSaveDTO {

    private String userName;
}
