package com.blog.bloggy.common.service;

import com.blog.bloggy.common.util.TokenUtil;
import com.blog.bloggy.token.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenUtil tokenUtil;



    public TokenDto reGenerateAccessToken(String userId){
        return tokenUtil.reGenerateAccessToken(userId);
    }
}
