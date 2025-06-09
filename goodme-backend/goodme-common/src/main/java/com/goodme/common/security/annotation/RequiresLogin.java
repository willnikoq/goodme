package com.goodme.common.security.annotation;

import java.lang.annotation.*;

/**
 * 登录校验注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresLogin {
} 