package com.blog.bloggy.interceptor;

import com.blog.bloggy.common.util.TokenUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.blog.bloggy.common.util.TokenUtil.*;
@Component
public class AccessInterceptor extends AuthInterceptor {

    public AccessInterceptor(TokenUtil tokenUtil) {
        super(tokenUtil);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }

    @Override
    protected void checkTokenValid() {
        tokenUtil.isExpired(this.token);
        tokenUtil.isValidType(this.token,ACCESS_TOKEN_TYPE);
    }
    @Override
    protected void setUserIdToAttribute(HttpServletRequest request) {
        String userId = tokenUtil.getUserIdFromToken(this.token);
        request.setAttribute(USER_ID_ATTRIBUTE_KEY, userId);
    }
}