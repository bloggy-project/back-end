package com.blog.bloggy.aop.token;


import com.blog.bloggy.common.util.TokenUtil;
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
import static com.blog.bloggy.common.util.TokenUtil.REFRESH_TOKEN_TYPE;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RefreshTokenRequiredAspect {

    private final TokenUtil tokenUtil;
    private final TokenService tokenService;

    @Around(value = "@annotation(refreshTokenRequired)")
    public Object loginCheck(ProceedingJoinPoint pjp, RefreshTokenRequired refreshTokenRequired) throws Throwable {
        String token = tokenService.getBearerTokenFromHeader();
        tokenService.checkTokenExist(token);

        String userId = tokenUtil.getUserIdFromToken(token);
        tokenUtil.isValidType(token,REFRESH_TOKEN_TYPE);
        tokenUtil.validRefreshToken(userId,token);

        Method method = MethodSignature.class.cast(pjp.getSignature()).getMethod();
        Object[] args = pjp.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < args.length; i++) {
            for (Annotation parameterAnnotation : parameterAnnotations[i]) {
                if (parameterAnnotation.annotationType() == Admin.class && method.getParameterTypes()[i] == String.class) {
                    args[i] = userId;
                }
            }
        }
        log.info("login User = {} ",userId);
        return pjp.proceed(args);
    }
}
