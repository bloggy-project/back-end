package com.blog.bloggy.aop.token;


import com.blog.bloggy.aop.masking.MaskingUtils;
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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static com.blog.bloggy.common.util.TokenUtil.ACCESS_TOKEN_TYPE;
import static com.blog.bloggy.common.util.TokenUtil.REFRESH_TOKEN_TYPE;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RefreshTokenRequiredAspect {

    private final TokenUtil tokenUtil;
    private final TokenService tokenService;
    private final UserService userService;
    private final MaskingUtils maskingUtils;


    @Around(value = "@annotation(refreshTokenRequired)")
    public Object loginCheck(ProceedingJoinPoint pjp, RefreshTokenRequired refreshTokenRequired) throws Throwable {
        String token = tokenService.getTokenFromCookie();
        tokenService.checkTokenExist(token);

        String userId = tokenUtil.getUserIdFromToken(token);
        tokenUtil.isValidType(token,REFRESH_TOKEN_TYPE);
        tokenUtil.validRefreshToken(userId,token);

        //Method method = MethodSignature.class.cast(pjp.getSignature()).getMethod();
        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof TokenUserDto) {
                TokenUserDto user = userService.getTokenUserDtoByUserId(userId);
                args[i]=user;
                String name = ((TokenUserDto) args[i]).getName();
                ((TokenUserDto) args[i]).setName(maskingUtils.maskString(name));
                log.info("mask Dto = {} ",args[i]);
            }
            else if (args[i] instanceof TestMaskingDto) {
                TestMaskingDto user = userService.getTestMaskingDtoByUserId(userId);
                args[i]=user;
                String name = ((TestMaskingDto) args[i]).getName();
                ((TestMaskingDto) args[i]).setName(maskingUtils.maskString(name));
                log.info("mask Dto = {} ",args[i]);
            }
        }
        /*
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < args.length; i++) {
            for (Annotation parameterAnnotation : parameterAnnotations[i]) {
                if (parameterAnnotation.annotationType() == Admin.class && method.getParameterTypes()[i] == String.class) {
                    args[i] = userId;
                }
            }
        }
         */
        return pjp.proceed(args);
    }
}
