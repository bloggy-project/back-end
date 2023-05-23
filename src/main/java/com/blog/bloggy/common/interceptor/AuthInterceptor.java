package com.blog.bloggy.common.interceptor;


import com.blog.bloggy.common.exception.AccessTokenRequiredException;
import com.blog.bloggy.common.exception.RefreshTokenRequiredException;
import com.blog.bloggy.common.util.TokenUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.blog.bloggy.common.util.TokenUtil.*;

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
    abstract protected String getBearerTokenFromHeader(HttpServletRequest request);
    abstract protected void checkTokenExist();
    abstract protected void checkTokenValid();
    abstract protected void setUserIdToAttribute(HttpServletRequest request);
}