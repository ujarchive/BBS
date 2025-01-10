package com.example.demo.Config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<RequestCachingFilter> requestCachingFilter() {
        FilterRegistrationBean<RequestCachingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestCachingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(10); // MultipartResolver 이후에 실행되도록 설정
        return registrationBean;
    }
}
