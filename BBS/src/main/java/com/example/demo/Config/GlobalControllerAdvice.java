package com.easynetworks.lotteFactoring.Config;

import com.easynetworks.lotteFactoring.Common.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute("userInfo")
    public UserDetails getUserInfo(@AuthenticationPrincipal UserDetails userDetails){

        return userDetails;
    }
}
