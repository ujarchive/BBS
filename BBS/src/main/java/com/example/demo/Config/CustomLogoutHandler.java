package com.example.demo.Config;

import com.easynetworks.lotteFactoring.Domain.User;
import com.easynetworks.lotteFactoring.Repository.UserRepository;
import com.easynetworks.lotteFactoring.Service.UserHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@Component
public class CustomLogoutHandler implements LogoutHandler {
    @Autowired
    UserHistoryService userHistoryService;
    @Autowired
    UserRepository userRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails) {
            log.debug("Principal type: {}", principal.getClass().getName());
            Optional<User> user = userRepository.findByUsername(((UserDetails) principal).getUsername());
            if(user.isPresent()) userHistoryService.logout(request, response, user.get()); // 로그아웃 기록 저장
        }
    }
}
