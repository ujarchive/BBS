package com.example.demo.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RequestCachingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
            // Content-Type 확인: multipart 요청인 경우 캐싱 생략
            filterChain.doFilter(request, response); // 캐싱 없이 필터 체인 전달
        } else if (request instanceof HttpServletRequest) {
            HttpServletRequest cachedRequest = new CachedBodyHttpServletRequest((HttpServletRequest) request);
            filterChain.doFilter(cachedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
