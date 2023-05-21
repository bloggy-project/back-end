package com.blog.summer.common.service;

import com.blog.summer.common.util.TokenUtil;
import com.blog.summer.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenUtil tokenUtil;



    public TokenDto reGenerateAccessToken(String userId,String refreshToken){
        return tokenUtil.reGenerateAccessToken(userId,refreshToken);
    }
}
