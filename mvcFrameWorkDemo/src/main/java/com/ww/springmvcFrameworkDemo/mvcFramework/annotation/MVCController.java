package com.ww.springmvcFrameworkDemo.mvcFramework.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MVCController {
    String value() default "";
}
