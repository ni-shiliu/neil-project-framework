package com.neil.myth.annotation;

import lombok.Getter;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Getter
public enum PropagationEnum {

    PROPAGATION_REQUIRED(0),

    PROPAGATION_SUPPORTS(1),

    PROPAGATION_MANDATORY(2),

    PROPAGATION_REQUIRES_NEW(3),

    PROPAGATION_NOT_SUPPORTED(4),

    PROPAGATION_NEVER(5),

    PROPAGATION_NESTED(6);

    private final int value;

    PropagationEnum(final int value) {
        this.value = value;
    }

}
