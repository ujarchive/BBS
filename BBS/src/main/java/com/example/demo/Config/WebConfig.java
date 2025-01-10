package com.example.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /**
         * addMaping: 해당 설정을 적용할 API 범위 선택 (/** -> 전체 적용)
         * allowedOrigins: Origin을 허용할 범위 선택 (생략 시 *와 같은 의미로 전체 허용됨)
         * allowedMethods: 허용할 HTTP 메서드 선택
         * exposedHeader: 서버에서 반환할 헤더 지정
         */
        registry.addMapping("/**")
                .allowedOrigins("https://192.168.10.50:4000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true) // 자격 증명 허용 여부
                .maxAge(3600); // preflight 요청 결과를 캐싱할 시간
    }
}
