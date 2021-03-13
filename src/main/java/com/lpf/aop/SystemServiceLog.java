package com.lpf.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface SystemServiceLog {
    String description() default "";
}
