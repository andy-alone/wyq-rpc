package com.wyq.rpc.core.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author wyq
 * @Date 2022-06-19 10:58
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface wyqScan {

    String[] basePackage();
}
