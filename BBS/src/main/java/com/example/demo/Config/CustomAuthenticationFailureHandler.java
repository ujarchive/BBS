package com.example.demo.Config;

import com.example.demo.DTO.UserDto;
import com.example.demo.Domain.ActivityType;
import com.example.demo.Domain.UserHistory;
import com.example.demo.Repository.UserHistoryRepository;
import com.example.demo.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserHistoryRepository userHistoryRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        CachedBodyHttpServletRequest cachedRequest = (CachedBodyHttpServletRequest) request;

        String requestBody = cachedRequest.getBody();
        UserDto userDto = objectMapper.readValue(requestBody, UserDto.class);
        String username = userDto.getUsername();

        String errMsg = "Invalid Username or Password-origin";

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        userRepository.findByUsername(username).isEmpty();
        if(username.isEmpty()){
            errMsg = "Username is missing";
        }else{
            userRepository.findByUsername(username).ifPresent(user -> {
                UserHistory history = UserHistory.builder()
                        .user(user)
                        .ip(request.getRemoteAddr()) // 클라이언트 IP 주소
                        .action(ActivityType.LOGIN_FAILURE)
                        .build();
                userHistoryRepository.save(history);
            });
            errMsg = "Invalid Password";
        }

        if(exception instanceof BadCredentialsException) {
            userRepository.findByUsername(username).ifPresent(user -> {
                user.setFailCount(user.getFailCount() + 1); // 실패 횟수 증가

                if (user.getFailCount() >= 5) { // 예: 최대 5번 허용
                    user.setEnabled(false);// 계정 잠금
                }
                userRepository.save(user); // 변경 사항 저장
            });
            errMsg = "Invalid Username or Password";
        } else if(exception instanceof DisabledException) {
            errMsg = "Locked";
        } else if(exception instanceof CredentialsExpiredException) {
            errMsg = "Expired password";
        }

        objectMapper.writeValue(response.getWriter(), errMsg);
    }
}
