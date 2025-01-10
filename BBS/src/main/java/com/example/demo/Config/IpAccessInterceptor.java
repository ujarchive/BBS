package com.example.demo.Config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class IpAccessInterceptor implements HandlerInterceptor {

//    @Autowired
//    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String clientIp = IpUtils.getClientIP(request);
//        if (clientIp.equals("127.0.0.1")) {
//            return true;
//        }
//
//        if (!userService.findByAccessIp(clientIp).isPresent()) {
//            log.warn("Forbidden access, URI: {}, IP: {}", request.getRequestURI(), clientIp);
//            response.sendError(403, "IP Forbidden");
//            return false;
//        }
        return true;
    }
}
