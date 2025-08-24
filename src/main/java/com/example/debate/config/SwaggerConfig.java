
package com.example.debate.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger(OpenAPI) 설정 클래스
 * - API 문서 기본 정보(타이틀, 버전, 설명) 제공
 */
@Configuration
public class SwaggerConfig {
    /**
     * OpenAPI 문서 정보 설정
     * @return OpenAPI 인스턴스
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Debate API Documentation")
                        .version("1.0.0")
                        .description("Comprehensive API documentation for Debate application"));
    }
}
