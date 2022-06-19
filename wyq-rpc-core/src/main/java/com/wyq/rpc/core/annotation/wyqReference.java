package com.wyq.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * @Author wyq
 * @Date 2022-06-19 11:12
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface wyqReference {

    String version() default "";

    String group() default "";
}
