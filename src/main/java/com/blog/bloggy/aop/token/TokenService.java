package com.blog.bloggy.aop.token;


import com.blog.bloggy.common.exception.RequiredTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
    protected void checkTokenExist(String token) {
        if (isEmpty(token)) {
            throw new RequiredTokenException();
        }
    }

}
