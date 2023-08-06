package com.blog.bloggy.common.controller;


import com.blog.bloggy.aop.token.AccessTokenRequired;
import com.blog.bloggy.aop.token.RefreshTokenRequired;
import com.blog.bloggy.aop.token.dto.AccessTokenDto;
import com.blog.bloggy.common.service.AuthService;
import com.blog.bloggy.common.util.TokenUtil;
import com.blog.bloggy.token.dto.TokenDto;
import com.blog.bloggy.user.dto.TestMaskingDto;
import com.blog.bloggy.user.dto.TokenUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final TokenUtil tokenUtil;


    /**
     * 토큰갱신
     *
     * @author gon
     * @param
     * @return accessToken, refreshToken
     */
    @RefreshTokenRequired
    @GetMapping(value="/refresh")
        public TokenDto refreshToken(TokenUserDto user, HttpServletResponse response) {
        log.info("do mask?: {}", user);
        return tokenUtil.reGenerateAccessToken(user.getUserId(),response);
    }

    @RefreshTokenRequired
    @GetMapping(value="/test/masking")
    public TestMaskingDto testMasking(TestMaskingDto user) {
        log.info("do mask?: {}", user);
        return user;
    }

    @RefreshTokenRequired
    @PostMapping(value="/logout")
    public ResponseEntity<String> logOut(TokenUserDto tokenUserDto){
        String response = authService.deleteToken(tokenUserDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
