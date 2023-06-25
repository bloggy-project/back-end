package com.blog.bloggy.aop.token;


import com.blog.bloggy.aop.token.dto.AccessTokenDto;
import com.blog.bloggy.common.util.TokenUtil;
import com.blog.bloggy.user.dto.TestMaskingDto;
import com.blog.bloggy.user.dto.TokenUserDto;
import com.blog.bloggy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static com.blog.bloggy.common.util.TokenUtil.ACCESS_TOKEN_TYPE;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AccessTokenRequiredAspect {

    private final TokenUtil tokenUtil;
    private final TokenService tokenService;

    @Around(value = "@annotation(accessTokenRequired)")
    public Object loginCheck(ProceedingJoinPoint pjp, AccessTokenRequired accessTokenRequired) throws Throwable {
        String token = tokenService.getBearerTokenFromHeader();
        tokenService.checkTokenExist(token);

        tokenUtil.isExpired(token);
        tokenUtil.isValidType(token,ACCESS_TOKEN_TYPE);

        String userId = tokenUtil.getUserIdFromToken(token);

        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof AccessTokenDto) {
                AccessTokenDto tokenDto=AccessTokenDto.builder()
                        .userId(userId)
                        .build();
                args[i]=tokenDto;
            }
        }

        log.info("login User = {} ",userId);
        return pjp.proceed(args);
     }
}
