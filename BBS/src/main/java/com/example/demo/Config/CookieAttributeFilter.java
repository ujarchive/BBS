package com.example.demo.Config;


import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
public class CookieAttributeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        chain.doFilter(request, response);
        addSameSite(httpServletResponse , "None");
    }

    private void addSameSite(HttpServletResponse response, String sameSite) {
        Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
        boolean firstHeader = true;
        for (String header : headers) { // there can be multiple Set-Cookie attributes
            if (firstHeader) {
                response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; Secure; %s", header, "SameSite=" + sameSite));
                firstHeader = false;
                continue;
            }
            response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; Secure; %s", header, "SameSite=" + sameSite));
        }
    }
}
