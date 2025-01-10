package com.example.demo.Utils;

import com.easynetworks.lotteFactoring.DTO.Message;
import org.springframework.ui.Model;

public class MessageUtils {
    public static String showMsgAndRedirect(Message params, Model model) {
        model.addAttribute("params", params);
        return "common/message";
    }
}
