package com.blog.summer.common.interceptor;

import com.blog.summer.common.exception.ExpiredTokenException;
import com.blog.summer.common.exception.RefreshTokenRequiredException;
import com.blog.summer.common.util.TokenUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.blog.summer.common.util.TokenUtil.*;
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
        this.setToken(this.getTokenFromHeader(request, REFRESH_TOKEN_HEADER));
        /*
            refresh값이 jwtToken의 value로 저장되므로
            io.jsonwebtoken.SignatureException: JWT signature does not match locally computed signature.
            값을 수정했더니 다음과 같은 오류가 발생.
         */
        setRefreshTokenToAttribute(request,token);
        return super.preHandle(request, response, handler);
    }

    private void setRefreshTokenToAttribute(HttpServletRequest request, String refreshToken) {
        request.setAttribute(REFRESH_TOKEN_HEADER,refreshToken);
    }
    @Override
    protected void checkTokenValid() {
        String userId = tokenUtil.getUserIdFromToken(this.token);
        tokenUtil.validRefreshToken(userId,this.token);
    }

    @Override
    protected void checkTokenExist() {
        if (isEmpty(this.token)) {
            throw new RefreshTokenRequiredException(this.uri);
        }
    }



}
