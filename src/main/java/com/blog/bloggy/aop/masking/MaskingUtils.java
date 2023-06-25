package com.blog.bloggy.aop.masking;

import org.springframework.stereotype.Component;

@Component
public class MaskingUtils {
    public String maskString(String input) {
        if (input != null && input.length() > 0) {
            String maskedName = input.substring(0, 1) + "**";
            input = maskedName;
        }
        return input;
    }
}