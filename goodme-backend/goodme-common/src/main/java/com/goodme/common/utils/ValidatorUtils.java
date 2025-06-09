package com.goodme.common.utils;

import java.util.regex.Pattern;

/**
 * 验证工具类
 */
public class ValidatorUtils {

    /**
     * 验证手机号
     *
     * @param mobile 手机号
     * @return 是否有效
     */
    public static boolean isMobile(String mobile) {
        if (mobile == null || mobile.length() != 11) {
            return false;
        }
        return Pattern.matches(RegexConstants.REGEX_MOBILE, mobile);
    }
    
    /**
     * 验证邮箱
     *
     * @param email 邮箱
     * @return 是否有效
     */
    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        }
        return Pattern.matches(RegexConstants.REGEX_EMAIL, email);
    }
    
    /**
     * 验证身份证号
     *
     * @param idCard 身份证号
     * @return 是否有效
     */
    public static boolean isIdCard(String idCard) {
        if (idCard == null) {
            return false;
        }
        return Pattern.matches(RegexConstants.REGEX_ID_CARD, idCard);
    }
    
    /**
     * 验证短信验证码
     *
     * @param code 验证码
     * @return 是否有效
     */
    public static boolean isSmsCode(String code) {
        if (code == null) {
            return false;
        }
        return Pattern.matches(RegexConstants.REGEX_SMS_CODE, code);
    }
} 