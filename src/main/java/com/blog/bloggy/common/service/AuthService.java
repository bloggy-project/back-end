package com.blog.bloggy.common.service;

import com.blog.bloggy.aop.token.dto.AccessTokenDto;
import com.blog.bloggy.common.util.TokenUtil;
import com.blog.bloggy.token.dto.TokenDto;
import com.blog.bloggy.token.repository.TokenRepository;
import com.blog.bloggy.user.dto.TokenUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenRepository tokenRepository;
    private final TokenUtil tokenUtil;

    public TokenDto reGenerateAccessToken(String userId, HttpServletResponse response){
        return tokenUtil.reGenerateAccessToken(userId,response);
    }
    public String deleteToken(TokenUserDto tokenUserDto){
        tokenRepository.deleteById(tokenUserDto.getUserId());

        return "logout";
    }


}
