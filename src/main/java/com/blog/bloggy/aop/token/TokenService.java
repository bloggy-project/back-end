package com.blog.bloggy.aop.token;


import com.blog.bloggy.common.exception.RequiredTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenService {


    protected String getBearerTokenFromHeader(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestHeader = request.getHeader("Authorization");
        String token="";
        if(requestHeader!=null&& requestHeader.startsWith("Bearer")){
            token= requestHeader.substring(7, requestHeader.length());
        }else{
            throw new RequiredTokenException();
        }
        return token;
    }
    protected String getTokenFromCookie(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        String refreshToken =null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 쿠키 정보 처리
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        return refreshToken;
    }

    protected void checkTokenExist(String token) {
        if (isEmpty(token)) {
            throw new RequiredTokenException();
        }
    }

}
