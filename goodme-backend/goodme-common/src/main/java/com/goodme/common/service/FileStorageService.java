package com.goodme.common.service;

import com.goodme.common.exception.BusinessException;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 文件存储服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.images:goodme-images}")
    private String imageBucket;

    @Value("${minio.bucket.videos:goodme-videos}")
    private String videoBucket;

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @return 图片URL
     */
    public String uploadImage(MultipartFile file) {
        return uploadFile(file, imageBucket);
    }

    /**
     * 上传视频
     *
     * @param file 视频文件
     * @return 视频URL
     */
    public String uploadVideo(MultipartFile file) {
        return uploadFile(file, videoBucket);
    }

    /**
     * 上传文件
     *
     * @param file       文件
     * @param bucketName 存储桶名称
     * @return 文件URL
     */
    public String uploadFile(MultipartFile file, String bucketName) {
        try {
            // 检查文件
            if (file == null || file.isEmpty()) {
                throw new BusinessException("上传文件不能为空");
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String fileName = generateFileName(originalFilename);

            // 检查存储桶是否存在，不存在则创建
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );

            // 获取文件访问URL
            return getFileUrl(bucketName, fileName);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件URL
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件URL
     */
    public String getFileUrl(String bucketName, String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .method(Method.GET)
                            .expiry(7, TimeUnit.DAYS)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取文件URL失败", e);
            throw new BusinessException("获取文件URL失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     */
    public void deleteFile(String bucketName, String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            log.error("删除文件失败", e);
            throw new BusinessException("删除文件失败: " + e.getMessage());
        }
    }

    /**
     * 生成唯一文件名
     *
     * @param originalFilename 原始文件名
     * @return 新文件名
     */
    private String generateFileName(String originalFilename) {
        // 获取文件扩展名
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // 使用UUID生成唯一文件名
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
} 