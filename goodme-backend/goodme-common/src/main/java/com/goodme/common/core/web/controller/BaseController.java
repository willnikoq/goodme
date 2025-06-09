package com.goodme.common.core.web.controller;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础控制器
 */
public class BaseController {
    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    protected Long getUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Object userId = request.getAttribute("userId");
            if (userId != null) {
                return (Long) userId;
            }
        }
        return null;
    }

    // 可扩展通用方法
} 