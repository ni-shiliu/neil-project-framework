package com.neil.myth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nihao
 * @date 2024/6/7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MythConsumer {

    String destination() default "";

    String tags() default "";

    Class target() default Object.class;

    String targetMethod() default "";

    PropagationEnum propagation() default PropagationEnum.PROPAGATION_REQUIRED;

    MessageTypeEnum pattern() default MessageTypeEnum.P2P;
}
