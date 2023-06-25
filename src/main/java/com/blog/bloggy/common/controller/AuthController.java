package com.blog.bloggy.common.controller;


import com.blog.bloggy.aop.token.RefreshTokenRequired;
import com.blog.bloggy.common.service.AuthService;
import com.blog.bloggy.token.dto.TokenDto;
import com.blog.bloggy.user.dto.TestMaskingDto;
import com.blog.bloggy.user.dto.TokenUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(value = "/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;


    /**
     * 토큰갱신
     *
     * @author gon
     * @param
     * @return accessToken, refreshToken
     */
    @RefreshTokenRequired
    @GetMapping(value="/refresh")
        public TokenDto refreshToken(TokenUserDto user) {
        log.info("do mask?: {}", user);
        return authService.reGenerateAccessToken(user.getUserId());
    }

    @RefreshTokenRequired
    @GetMapping(value="/test/masking")
    public TestMaskingDto testMasking(TestMaskingDto user) {
        log.info("do mask?: {}", user);
        return user;
    }
}
