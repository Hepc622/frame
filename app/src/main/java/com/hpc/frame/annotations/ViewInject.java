package com.hpc.frame.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @description : 自动注入控件注解
 * @author : HPC
 * @date : 2019/7/12 11:17
 */
@Target({ElementType.FIELD,ElementType.TYPE})//表示用在字段上
@Retention(RetentionPolicy.RUNTIME)//表示在生命周期是运行时
public @interface ViewInject {
    int value() default 0;
}