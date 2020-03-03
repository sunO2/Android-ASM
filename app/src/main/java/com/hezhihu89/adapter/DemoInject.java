package com.hezhihu89.adapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hezhihu89
 * 创建时间 2020 年 03 月 03 日 14:24
 * 功能描述:
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface DemoInject {
}
