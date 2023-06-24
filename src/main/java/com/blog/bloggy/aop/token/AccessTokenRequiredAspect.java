package com.blog.bloggy.aop.token;


import com.blog.bloggy.common.util.TokenUtil;
import com.blog.bloggy.token.model.Token;
import com.blog.bloggy.user.dto.TokenUserDto;
import com.blog.bloggy.user.repository.UserRepository;
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
    private final AccessTokenService accessTokenService;
    private final UserService userService;

    @Around(value = "@annotation(accessTokenRequired)")
    public Object loginCheck(ProceedingJoinPoint pjp, AccessTokenRequired accessTokenRequired) throws Throwable {
        String token = accessTokenService.getBearerTokenFromHeader();
        accessTokenService.checkTokenExist(token);
        tokenUtil.isExpired(token);
        tokenUtil.isValidType(token,ACCESS_TOKEN_TYPE);
        String userId = tokenUtil.getUserIdFromToken(token);
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
