package com.example.demo.Config;

import com.easynetworks.lotteFactoring.Exception.ApiExceptionEntity;
import com.easynetworks.lotteFactoring.Exception.ApiResult;
import com.easynetworks.lotteFactoring.Exception.ErrorHandling;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginAuthenticationEntrypoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ErrorHandling.ACCESS_DENIED_EXCEPTION.getCode())
                .errorMessage(ErrorHandling.ACCESS_DENIED_EXCEPTION.getMessage())
                .build();

        ApiResult apiResult = ApiResult.builder()
                .status("ERROR")
                .message("")
                .exception(apiExceptionEntity)
                .build();

        // JSON 형식으로 응답
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드
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
