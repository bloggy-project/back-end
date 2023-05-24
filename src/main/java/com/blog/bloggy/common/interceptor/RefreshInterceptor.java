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
        //getUserIdFromToken -> Token parse하면서 내부적으로 만료나 토큰 value 관련 예외 처리.
        String userId = tokenUtil.getUserIdFromToken(this.token);
        tokenUtil.isValidType(this.token,REFRESH_TOKEN_TYPE);
        tokenUtil.validRefreshToken(userId,this.token);
    }
    @Override
    protected void setUserIdToAttribute(HttpServletRequest request) {
        String userId = tokenUtil.getUserIdFromToken(this.token);
        request.setAttribute(USER_ID_ATTRIBUTE_KEY, userId);
    }

}
