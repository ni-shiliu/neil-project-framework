package com.neil.myth.annotation;

import java.lang.annotation.*;

/**
 * @author nihao
 * @date 2024/6/7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MythSPI {

    String value() default "";
}
