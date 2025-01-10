package com.easynetworks.lotteFactoring.Config;

import com.easynetworks.lotteFactoring.Domain.User;
import com.easynetworks.lotteFactoring.Repository.UserRepository;
import com.easynetworks.lotteFactoring.Service.UserHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserHistoryService userHistoryService;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        // 실패 횟수 초기화
        userRepository.findByUsername(username).ifPresent(user1 -> {
            user1.setFailCount(0); // 실패 횟수 초기화
            user1.setAccessDate(LocalDateTime.now());
            user1.setAccessIp(request.getRemoteAddr());
            userRepository.save(user1);
        });


        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        objectMapper.writeValue(response.getWriter(), user);
        userHistoryService.save(request, response, user);
    }
}
