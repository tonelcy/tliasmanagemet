package com.lcy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc OpenAPI 配置类（兼容 Spring Boot 2.6+）
 *
 * 说明：SpringDoc 是官方推荐的 OpenAPI 3.0 实现，完美兼容 Spring Boot 2.6+
 * 访问地址：http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI createOpenAPI() {
        return new OpenAPI()
                // API 信息
                .info(apiInfo())
                // 安全认证配置（Token）
                .addSecurityItem(new SecurityRequirement().addList("token"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("token")));
    }

    private Info apiInfo() {
        return new Info()
                .title("Tlias 管理系统 API 文档")
                .description("Tlias 管理系统接口文档 - 基于 SpringDoc OpenAPI 3.0")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Tlias Team")
                        .email(""))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }
}
