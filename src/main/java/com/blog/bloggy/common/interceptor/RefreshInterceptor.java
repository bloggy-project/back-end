package com.blog.bloggy.common.interceptor;

import com.blog.bloggy.common.exception.AccessTokenRequiredException;
import com.blog.bloggy.common.exception.RefreshTokenRequiredException;
import com.blog.bloggy.common.util.TokenUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.blog.bloggy.common.util.TokenUtil.*;
import static org.springframework.util.ObjectUtils.isEmpty;

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
    protected String getBearerTokenFromHeader(HttpServletRequest request) {
        String requestHeader = request.getHeader("Authorization");
        String token="";
        if(requestHeader!=null&& requestHeader.startsWith("Bearer")){
            token= requestHeader.substring(7, requestHeader.length());
        }else{
            throw new RefreshTokenRequiredException(this.uri);
        }
        return token;
    }
    @Override
    protected void checkTokenExist() {
        if (isEmpty(this.token)) {
            throw new RefreshTokenRequiredException(this.uri);
        }
    }
    @Override
    protected void checkTokenValid() {
        String userId = tokenUtil.getUserIdFromRefreshToken(this.token);
        tokenUtil.validRefreshToken(userId,this.token);
    }
    @Override
    protected void setUserIdToAttribute(HttpServletRequest request) {
        String userId = tokenUtil.getUserIdFromRefreshToken(this.token);
        request.setAttribute(USER_ID_ATTRIBUTE_KEY, userId);
    }

}
