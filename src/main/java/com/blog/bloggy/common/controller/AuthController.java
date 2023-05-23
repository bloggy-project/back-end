package com.blog.bloggy.common.controller;


import com.blog.bloggy.common.service.AuthService;
import com.blog.bloggy.common.util.TokenUtil;
import com.blog.bloggy.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping(value="/refresh")
        public TokenDto refreshToken(@RequestAttribute(USER_ID_ATTRIBUTE_KEY) String userId) {
        log.info("refreshToken userId ::: {}", userId);
        return authService.reGenerateAccessToken(userId);
    }
}
