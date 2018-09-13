package com.ww.springmvcFrameworkDemo.mvcFramework.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MVCRequestMapping {
    String value() default "";
}
