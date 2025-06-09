package com.goodme.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录响应")
public class LoginResponse {
    
    @Schema(description = "用户token")
    private String token;
    
    @Schema(description = "token有效期（秒）")
    private Long expireIn;
    
    @Schema(description = "用户信息")
    private UserDTO userInfo;
} 