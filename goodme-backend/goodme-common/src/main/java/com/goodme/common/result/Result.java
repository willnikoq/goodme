package com.goodme.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果类
 *
 * @param <T> 数据类型
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 私有构造方法，禁止直接创建
     */
    private Result() {
    }

    /**
     * 判断是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.code);
    }

    /**
     * 成功返回结果
     *
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功返回结果
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(T data) {
        return success(ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param message 提示信息
     * @param data    数据
     * @param <T>     数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败返回结果
     *
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * 失败返回结果
     *
     * @param resultCode 错误码
     * @param <T>        数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(IResultCode resultCode) {
        return failed(resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     * @param <T>     数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(String message) {
        return failed(ResultCode.FAILED.getCode(), message);
    }

    /**
     * 失败返回结果
     *
     * @param code    状态码
     * @param message 提示信息
     * @param <T>     数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 参数验证失败返回结果
     *
     * @param <T> 数据类型
     * @return 参数验证失败结果
     */
    public static <T> Result<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     * @param <T>     数据类型
     * @return 参数验证失败结果
     */
    public static <T> Result<T> validateFailed(String message) {
        return failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    /**
     * 未登录返回结果
     *
     * @param <T> 数据类型
     * @return 未登录结果
     */
    public static <T> Result<T> unauthorized() {
        return failed(ResultCode.UNAUTHORIZED);
    }

    /**
     * 未授权返回结果
     *
     * @param <T> 数据类型
     * @return 未授权结果
     */
    public static <T> Result<T> forbidden() {
        return failed(ResultCode.FORBIDDEN);
    }
} 