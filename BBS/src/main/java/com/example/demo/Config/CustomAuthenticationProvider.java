package com.example.demo.Config;

import com.easynetworks.lotteFactoring.Domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = (User) userDetailsService.loadUserByUsername(loginId);

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        //인증된 Token생성
        return new CustomAuthenticationToken(user,null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CustomAuthenticationToken.class);
    }
}
