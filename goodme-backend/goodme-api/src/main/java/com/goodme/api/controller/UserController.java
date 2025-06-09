package com.goodme.api.controller;

import com.goodme.common.result.Result;
import com.goodme.service.dto.UserDTO;
import com.goodme.service.entity.User;
import com.goodme.service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户控制器
 */
@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "获取当前登录用户信息", description = "根据Token获取当前登录用户的详细信息")
    @GetMapping("/info")
    public Result<UserDTO> getCurrentUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized();
        }
        return Result.success(userService.getUserInfo(userId));
    }

    @Operation(summary = "更新用户信息", description = "更新当前登录用户的个人信息")
    @PutMapping("/info")
    public Result<Boolean> updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized();
        }
        
        // 确保用户只能更新自己的信息
        user.setId(userId);
        return Result.success(userService.updateUserInfo(user));
    }
} 