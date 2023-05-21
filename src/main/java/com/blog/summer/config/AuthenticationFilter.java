package com.blog.summer.config;

import com.blog.summer.common.util.TokenUtil;
import com.blog.summer.dto.user.RequestLogin;
import com.blog.summer.dto.user.UserDto;
import com.blog.summer.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.blog.summer.common.util.TokenUtil.*;


@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment env;

    private TokenUtil tokenUtil;


    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService
            , Environment env,TokenUtil tokenUtil) {
        super(authenticationManager);
        this.userService = userService;
        this.env = env;
        this.tokenUtil=tokenUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {


        try {
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(username);
        String userId = userDetails.getUserId();
        log.info("userId ::: {}",userId);
        String accessToken = tokenUtil.generateAccessToken(userId);
        String refreshToken = tokenUtil.generateRefreshToken(userId);
        response.addHeader(ACCESS_TOKEN_HEADER, accessToken);
        response.addHeader(REFRESH_TOKEN_HEADER,refreshToken);
    }
}
