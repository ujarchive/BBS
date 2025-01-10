package com.easynetworks.lotteFactoring.Config;

import com.easynetworks.lotteFactoring.DTO.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public CustomAuthenticationFilter() {
        // url과 일치할 경우 해당 필터가 동작합니다.
        super(new AntPathRequestMatcher("/api/login"));
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        // 해당 요청이 POST 인지 확인
        if(!isPost(request)) {
            throw new IllegalStateException("Authentication is not supported");
        }

        CachedBodyHttpServletRequest cachedRequest = (CachedBodyHttpServletRequest) request;

        // POST 이면 body 를 UserDto( 로그인 정보 DTO ) 에 매핑
        String requestBody = cachedRequest.getBody();
        UserDto userDto = objectMapper.readValue(requestBody, UserDto.class);

        // ID, PASSWORD 가 있는지 확인
        if(!StringUtils.hasLength(userDto.getUsername())
                || !StringUtils.hasLength(userDto.getPassword())) {
            throw new IllegalArgumentException("username or password is empty");
        }

        // 처음에는 인증 되지 않은 토큰 생성
        CustomAuthenticationToken token = new CustomAuthenticationToken(
                userDto.getUsername(),
                userDto.getPassword()
        );

        // Manager 에게 인증 처리
        Authentication authenticate = getAuthenticationManager().authenticate(token);
        return authenticate;
    }

    private boolean isPost(HttpServletRequest request) {

        if("POST".equals(request.getMethod())) {
            return true;
        }

        return false;
    }

}
