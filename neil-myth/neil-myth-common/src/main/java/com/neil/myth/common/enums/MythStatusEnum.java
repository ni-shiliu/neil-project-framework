package com.neil.myth.common.enums;


import lombok.Getter;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Getter
public enum MythStatusEnum {

    BEGIN("BEGIN", "开始"),

    COMMIT("COMMIT", "已经提交"),

    FAILURE("FAILURE", "失败");

    private String code;

    private String desc;

    MythStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
