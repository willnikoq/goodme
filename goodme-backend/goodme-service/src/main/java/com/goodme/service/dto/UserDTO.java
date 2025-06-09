package com.goodme.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户数据传输对象
 */
@Data
@Schema(description = "用户信息")
public class UserDTO {
    
    @Schema(description = "用户ID")
    private Long id;
    
    @Schema(description = "手机号")
    private String phone;
    
    @Schema(description = "昵称")
    private String nickname;
    
    @Schema(description = "头像URL")
    private String avatar;
    
    @Schema(description = "性别：0未知，1男，2女")
    private Integer gender;
    
    @Schema(description = "生日")
    private LocalDate birthday;
    
    @Schema(description = "会员等级ID")
    private Long memberLevelId;
    
    @Schema(description = "会员等级名称")
    private String memberLevelName;
    
    @Schema(description = "积分")
    private Integer points;
    
    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;
} 