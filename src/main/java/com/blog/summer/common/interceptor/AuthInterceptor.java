package com.blog.summer.common.interceptor;


import com.blog.summer.common.util.TokenUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.blog.summer.common.util.TokenUtil.USER_ID_ATTRIBUTE_KEY;

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
        checkTokenExist();
        checkTokenValid();
        setUserIdToAttribute(request);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    abstract protected void checkTokenExist();

    protected String getTokenFromHeader(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }

    abstract protected void checkTokenValid();

    private void setUserIdToAttribute(HttpServletRequest request) {
        String userId = tokenUtil.getUserIdFromToken(this.token);
        request.setAttribute(USER_ID_ATTRIBUTE_KEY, userId);
    }
}