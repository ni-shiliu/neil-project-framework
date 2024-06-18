package com.neil.myth.common.enums;

import lombok.Getter;

/**
 * @author nihao
 * @date 2024/6/12
 */
@Getter
public enum MythRoleEnum {

    START("START", "发起者"),

    LOCAL("LOCAL", "本地执行"),

    PROVIDER("PROVIDER", "提供者");

    private String code;

    private String desc;

    MythRoleEnum(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MythRoleEnum byCode(final String code) {
        for (MythRoleEnum item : MythRoleEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

}
