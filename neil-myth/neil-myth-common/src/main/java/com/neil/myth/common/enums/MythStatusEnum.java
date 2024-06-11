package com.neil.myth.common.enums;


import lombok.Getter;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Getter
public enum MythStatusEnum {

    ROLLBACK("ROLLBACK", "回滚"),

    COMMIT("COMMIT", "已经提交"),

    BEGIN("BEGIN", "开始"),

    SEND_MSG("SEND_MSG", "可以发送消息"),

    FAILURE("FAILURE", "失败"),

    PRE_COMMIT("PRE_COMMIT", "预提交"),

    LOCK("LOCK", "锁定");

    private String code;

    private String desc;

    MythStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
