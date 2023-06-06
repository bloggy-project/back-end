package com.blog.bloggy.interceptor;

import com.blog.bloggy.common.exception.RequiredTokenException;
import com.blog.bloggy.common.util.TokenUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
abstract public class AuthInterceptor implements HandlerInterceptor {

    protected final TokenUtil tokenUtil;

    @Getter
    @Setter
    protected String token;

    @Getter
    @Setter
    protected String uri;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.setUri(request.getRequestURI());
        this.setToken(this.getBearerTokenFromHeader(request));
        checkTokenExist();
        checkTokenValid();
        setUserIdToAttribute(request);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    protected String getBearerTokenFromHeader(HttpServletRequest request){
        String requestHeader = request.getHeader("Authorization");
        String token="";
        if(requestHeader!=null&& requestHeader.startsWith("Bearer")){
            token= requestHeader.substring(7, requestHeader.length());
        }else{
            throw new RequiredTokenException(this.uri);
        }
        return token;
    }
    protected void checkTokenExist() {
        if (isEmpty(this.token)) {
            throw new RequiredTokenException(this.uri);
        }
    }
    abstract protected void checkTokenValid();
    abstract protected void setUserIdToAttribute(HttpServletRequest request);
}