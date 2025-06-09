package com.goodme.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 登录请求对象
 */
@Data
@Schema(description = "登录请求")
public class LoginRequest {
    
    @Schema(description = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Schema(description = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
    
    @Schema(description = "登录类型：1-密码登录，2-验证码登录，3-微信登录", defaultValue = "1")
    private Integer loginType = 1;
    
    @Schema(description = "验证码")
    @Pattern(regexp = "^\\d{6}$", message = "验证码格式不正确")
    private String code;
    
    @Schema(description = "微信code")
    private String wxCode;
} 