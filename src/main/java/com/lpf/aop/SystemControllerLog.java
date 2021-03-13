package com.lpf.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface SystemControllerLog {

    String logModel() default "";//操作模块
    String description() default ""; //描述
    String actionType() default ""; //操作类型：1添加，2修改，3删除，4查询

}
