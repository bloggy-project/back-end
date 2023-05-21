package com.blog.summer.common.interceptor;

import com.blog.summer.common.exception.AccessTokenRequiredException;
import com.blog.summer.common.exception.ExpiredTokenException;
import com.blog.summer.common.util.TokenUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.ObjectUtils.isEmpty;
import static com.blog.summer.common.util.TokenUtil.ACCESS_TOKEN_HEADER;

@Component
public class AccessInterceptor extends AuthInterceptor {



    public AccessInterceptor(TokenUtil tokenUtil) {
        super(tokenUtil);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        this.setToken(this.getTokenFromHeader(request, ACCESS_TOKEN_HEADER));
        return super.preHandle(request, response, handler);
    }

    @Override
    protected void checkTokenValid() {
        tokenUtil.isExpired(this.token);
    }
    @Override
    protected void checkTokenExist() {
        if (isEmpty(this.token)) {
            throw new AccessTokenRequiredException(this.uri);
        }
    }

}