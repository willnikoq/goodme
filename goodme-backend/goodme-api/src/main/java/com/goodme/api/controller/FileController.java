package com.goodme.api.controller;

import com.goodme.common.result.Result;
import com.goodme.common.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 */
@Tag(name = "文件管理", description = "文件上传相关接口")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @Operation(summary = "上传图片", description = "上传图片文件，返回图片URL")
    @PostMapping("/upload/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = fileStorageService.uploadImage(file);
        return Result.success(imageUrl);
    }

    @Operation(summary = "上传视频", description = "上传视频文件，返回视频URL")
    @PostMapping("/upload/video")
    public Result<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        String videoUrl = fileStorageService.uploadVideo(file);
        return Result.success(videoUrl);
    }
} 