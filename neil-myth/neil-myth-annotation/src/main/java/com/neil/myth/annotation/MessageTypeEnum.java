package com.neil.myth.annotation;

import lombok.Getter;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Getter
public enum MessageTypeEnum {

    P2P(1, "点对点模式"),

    TOPIC(2, "TOPIC模式");

    private final Integer code;

    private final String desc;

    MessageTypeEnum(final Integer code, final String desc) {
        this.code = code;
        this.desc = desc;
    }
}
