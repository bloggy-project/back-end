package com.blog.bloggy.filter;

import com.blog.bloggy.common.util.TokenUtil;
import com.blog.bloggy.user.dto.RequestLogin;
import com.blog.bloggy.user.dto.UserDto;
import com.blog.bloggy.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
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
import java.util.HashMap;
import java.util.Map;



@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;

    private TokenUtil tokenUtil;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService
            ,TokenUtil tokenUtil) {
        super(authenticationManager);
        this.userService = userService;
        this.tokenUtil=tokenUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {


        try {
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            log.info("로그인 시도 *********************************************");
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            e.printStackTrace();
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

        /*
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(refreshTokenCookie);

         */

        Map<String, String> responseJson = new HashMap<>();
        responseJson.put("accessToken", accessToken);
        responseJson.put("thumbnail", userDetails.getThumbnail());
        responseJson.put("email", userDetails.getEmail());
        responseJson.put("name", userDetails.getName());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseJson));

        // Refresh Token을 쿠키로 설정하여 응답
        ResponseCookie cookie = tokenUtil.getResponseCookie(refreshToken);
        response.addHeader("Set-Cookie", cookie.toString());
        //response.addHeader("accessToken", accessToken);
    }


}
