package com.ww.springmvcFrameworkDemo.mvcFramework.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MVCService {
    String value() default "";
}
