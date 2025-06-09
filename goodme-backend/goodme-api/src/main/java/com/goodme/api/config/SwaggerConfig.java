package com.goodme.api.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * OpenAPI配置类
 * 
 * @author goodme
 */
@Configuration
public class SwaggerConfig {

    @Primary
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("古茗奶茶API文档")
                        .description("古茗奶茶小程序后端接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("goodme")
                                .url("http://www.goodme.com")
                                .email("admin@goodme.com")));
    }

    @Bean
    public GroupedOpenApi cartApi() {
        return GroupedOpenApi.builder()
                .group("购物车管理")
                .pathsToMatch("/api/cart/**")
                .build();
    }
} 