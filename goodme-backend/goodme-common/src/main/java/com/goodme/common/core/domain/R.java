package com.goodme.common.core.domain;

import com.goodme.common.result.Result;

public class R {
    public static Result<Object> ok() { return Result.success(); }
    public static Result<Object> ok(Object data) { return Result.success(data); }
    public static Result<Object> fail(String message) { return Result.failed(message); }
} 