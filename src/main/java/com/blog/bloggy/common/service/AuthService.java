package com.blog.bloggy.common.service;

import com.blog.bloggy.common.util.TokenUtil;
import com.blog.bloggy.token.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenUtil tokenUtil;

    public TokenDto reGenerateAccessToken(String userId, HttpServletResponse response){
        return tokenUtil.reGenerateAccessToken(userId,response);
    }
}
