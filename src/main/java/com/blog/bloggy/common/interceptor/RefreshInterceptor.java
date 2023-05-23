package com.blog.bloggy.common.interceptor;

import com.blog.bloggy.common.exception.InvalidTokenTypeException;
import com.blog.bloggy.common.util.TokenUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.blog.bloggy.common.util.TokenUtil.*;

@Component
public class RefreshInterceptor extends AuthInterceptor {


    public RefreshInterceptor(TokenUtil tokenUtil) {
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
        String userId = tokenUtil.getUserIdFromToken(this.token);
        if(!isValidType(REFRESH_TOKEN_TYPE)){
            throw new InvalidTokenTypeException();
        }
        tokenUtil.validRefreshToken(userId,this.token);
    }
    @Override
    protected void setUserIdToAttribute(HttpServletRequest request) {
        String userId = tokenUtil.getUserIdFromToken(this.token);
        request.setAttribute(USER_ID_ATTRIBUTE_KEY, userId);
    }

}
