package com.blog.bloggy.common.controller;


import com.blog.bloggy.aop.token.Admin;
import com.blog.bloggy.aop.token.RefreshTokenRequired;
import com.blog.bloggy.common.service.AuthService;
import com.blog.bloggy.token.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.blog.bloggy.common.util.TokenUtil.USER_ID_ATTRIBUTE_KEY;


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
     * @param userId
     * @return accessToken, refreshToken
     */
    @RefreshTokenRequired
    @GetMapping(value="/refresh")
        public TokenDto refreshToken(@Admin String userId) {
        return authService.reGenerateAccessToken(userId);
    }
}
