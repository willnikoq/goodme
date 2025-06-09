package com.goodme.api.controller;

import com.goodme.common.exception.BusinessException;
import com.goodme.common.result.Result;
import com.goodme.common.utils.ValidatorUtils;
import com.goodme.service.dto.LoginRequest;
import com.goodme.service.dto.LoginResponse;
import com.goodme.service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Tag(name = "认证管理", description = "用户认证相关接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "用户登录", description = "支持密码、验证码、微信三种登录方式")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @Operation(summary = "发送短信验证码", description = "给指定手机号发送短信验证码")
    @GetMapping("/sms/code")
    public Result<Boolean> sendSmsCode(@RequestParam String phone) {
        // 验证手机号格式
        if (!ValidatorUtils.isMobile(phone)) {
            throw new BusinessException("手机号格式不正确");
        }
        return Result.success(userService.sendSmsCode(phone));
    }

    @Operation(summary = "用户注册", description = "使用手机号、密码和验证码进行注册")
    @PostMapping("/register")
    public Result<Boolean> register(
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam String code) {
        // 验证手机号格式
        if (!ValidatorUtils.isMobile(phone)) {
            throw new BusinessException("手机号格式不正确");
        }
        // 验证验证码格式
        if (!ValidatorUtils.isSmsCode(code)) {
            throw new BusinessException("验证码格式不正确");
        }
        return Result.success(userService.register(phone, password, code));
    }

    @Operation(summary = "微信登录", description = "使用微信授权code进行登录")
    @GetMapping("/wx/login")
    public Result<LoginResponse> wxLogin(@RequestParam String code) {
        return Result.success(userService.wxLogin(code));
    }
} 