package com.blog.summer.common.controller;


import com.blog.summer.common.service.AuthService;
import com.blog.summer.common.util.TokenUtil;
import com.blog.summer.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.blog.summer.common.util.TokenUtil.REFRESH_TOKEN_HEADER;
import static com.blog.summer.common.util.TokenUtil.USER_ID_ATTRIBUTE_KEY;

@RestController
@RequestMapping(value = "/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;


    /**
     * 토큰갱신
     *
     * @author hjkim
     * @param userId
     * @return accessToken, refreshToken
     */
    @GetMapping(value="/refresh")
        public TokenDto refreshToken(@RequestAttribute(USER_ID_ATTRIBUTE_KEY) String userId,
                                     @RequestAttribute(REFRESH_TOKEN_HEADER) String refreshToken) {
        log.info("refreshToken userId ::: {}", userId);
        log.info("refreshToken value ::: {}", refreshToken);
        return authService.reGenerateAccessToken(userId,refreshToken);
    }
}
