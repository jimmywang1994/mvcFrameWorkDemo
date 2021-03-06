package com.ww.springmvcFrameworkDemo.mvcFramework.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MVCAutowired {
    String value() default "";
}
