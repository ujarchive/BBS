package com.example.demo.Config;

import com.easynetworks.lotteFactoring.Exception.ApiExceptionEntity;
import com.easynetworks.lotteFactoring.Exception.ApiResult;
import com.easynetworks.lotteFactoring.Exception.ErrorHandling;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHander implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 기존 ApiResult와 ApiExceptionEntity 구조로 반환
        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ErrorHandling.FORBIDDEN_EXCEPTION.getCode())
                .errorMessage(ErrorHandling.FORBIDDEN_EXCEPTION.getMessage())
                .build();

        ApiResult apiResult = ApiResult.builder()
                .status("ERROR")
                .message("")
                .exception(apiExceptionEntity)
                .build();

        // JSON 형식으로 응답
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 상태 코드
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 데이터로 변환 후 출력
        response.getWriter().write(toJson(apiResult));
    }

    private String toJson(Object object) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            return "{}"; // JSON 변환 실패 시 빈 객체 반환
        }
    }
}
