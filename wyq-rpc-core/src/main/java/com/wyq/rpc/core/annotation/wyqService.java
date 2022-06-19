package com.wyq.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * @Author wyq
 * @Date 2022-06-19 11:10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface wyqService {

    String version() default "";

    String group() default "";
}
