package com.goodme.framework.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 配置类
 */
@Configuration
public class OpenAPIConfig {

    /**
     * 创建 OpenAPI 应用
     */
    @Bean
    public OpenAPI createOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .components(new Components()
                        .addSecuritySchemes("Authorization", securityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }

    /**
     * 创建 API 文档基本信息
     */
    private Info apiInfo() {
        return new Info()
                .title("古茗奶茶小程序 API 文档")
                .description("古茗奶茶小程序后端接口文档")
                .version("1.0.0")
                .contact(new Contact()
                        .name("GoodMe")
                        .url("http://www.goodme.com")
                        .email("contact@goodme.com"));
    }

    /**
     * 安全模式，这里指定 token 通过 Authorization 请求头传递
     */
    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }
} 