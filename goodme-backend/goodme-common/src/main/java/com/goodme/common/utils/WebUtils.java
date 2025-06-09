package com.goodme.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodme.common.result.Result;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Web工具类
 */
public class WebUtils {

    /**
     * 将对象写入响应
     *
     * @param response 响应对象
     * @param result   返回结果
     */
    public static void writeJsonResponse(HttpServletResponse response, Result<?> result) throws IOException {
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
} 