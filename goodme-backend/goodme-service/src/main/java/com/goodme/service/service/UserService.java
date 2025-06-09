package com.goodme.service.service;

import com.goodme.service.dto.LoginRequest;
import com.goodme.service.dto.LoginResponse;
import com.goodme.service.dto.UserDTO;
import com.goodme.service.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @return 是否发送成功
     */
    boolean sendSmsCode(String phone);

    /**
     * 注册新用户
     *
     * @param phone    手机号
     * @param password 密码
     * @param code     验证码
     * @return 是否注册成功
     */
    boolean register(String phone, String password, String code);

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserDTO getUserInfo(Long userId);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 是否更新成功
     */
    boolean updateUserInfo(User user);

    /**
     * 微信登录授权
     *
     * @param wxCode 微信授权码
     * @return 登录响应
     */
    LoginResponse wxLogin(String wxCode);
} 