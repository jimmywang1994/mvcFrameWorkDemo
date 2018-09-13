package com.ww.springmvcFrameworkDemo.mvcFramework.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MVCRequestParam {
    String value() default "";
}
