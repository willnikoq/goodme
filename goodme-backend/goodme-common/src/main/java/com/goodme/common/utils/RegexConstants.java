package com.goodme.common.utils;

/**
 * 正则表达式常量
 */
public class RegexConstants {

    /**
     * 手机号正则表达式（中国大陆）
     */
    public static final String REGEX_MOBILE = "^1[3-9]\\d{9}$";
    
    /**
     * 邮箱正则表达式
     */
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    
    /**
     * 身份证号正则表达式
     */
    public static final String REGEX_ID_CARD = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
    
    /**
     * 验证码正则表达式（6位数字）
     */
    public static final String REGEX_SMS_CODE = "^\\d{6}$";
} 