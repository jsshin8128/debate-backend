package com.example.debate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc CORS 정책 설정: 프론트엔드 개발 환경에서 API 접근 허용
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 모든 경로에 대해 CORS 허용 (localhost:3000)
     */
    @Override
    public void addCorsMappings(@org.springframework.lang.NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
    }
}
