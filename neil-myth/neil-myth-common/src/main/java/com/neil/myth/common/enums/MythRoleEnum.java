package com.neil.myth.common.enums;

import lombok.Getter;

/**
 * @author nihao
 * @date 2024/6/12
 */
@Getter
public enum MythRoleEnum {

    START(1, "发起者"),

    LOCAL(2, "本地执行"),

    PROVIDER(3, "提供者");

    private int code;

    private String desc;

    MythRoleEnum(final int code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

}
