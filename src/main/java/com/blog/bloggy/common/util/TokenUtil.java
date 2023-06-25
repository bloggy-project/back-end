package com.blog.bloggy.common.util;

import com.blog.bloggy.common.exception.*;
import com.blog.bloggy.token.model.Token;
import com.blog.bloggy.token.dto.TokenDto;
import com.blog.bloggy.token.repository.TokenRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenUtil {

    private final Environment env;
    private final TokenRepository tokenRepository;
    public static final String USER_ID_ATTRIBUTE_KEY = "userId";

    public static final String ACCESS_TOKEN_TYPE="ACCESS";
    public static final String REFRESH_TOKEN_TYPE="REFRESH";


    public String generateToken(String userId,String type){
        // 1. token 내부에 저장할 정보
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("type",type);
        // 2. token 생성일
        Date createdDate = new Date(); //현재 시간으로 생성.
        // 3. token 만료일
        Date expirationDate = new Date();
        Calendar cal=Calendar.getInstance();
        cal.setTime(expirationDate);
        if(type.equals(ACCESS_TOKEN_TYPE)){
            cal.add(Calendar.SECOND, Integer.parseInt(env.getProperty("token.expiration_time")));
            expirationDate = cal.getTime();
        }
        else if(type.equals(REFRESH_TOKEN_TYPE)){
            cal.add(Calendar.SECOND, Integer.parseInt(env.getProperty("refresh_token.random_time")));
            expirationDate = cal.getTime();
        }
        return Jwts.builder()
                .setClaims(claims)      // 1
                .setIssuedAt(createdDate)       // 2
                .setExpiration(expirationDate)      // 3
                .signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                .compact();
    }
    public String generateAccessToken(String userId) {
        return generateToken(userId,ACCESS_TOKEN_TYPE);
    }

    public String generateRefreshToken(String userId) {
        String refreshToken = generateToken(userId, REFRESH_TOKEN_TYPE);
        // Refresh Token 생성 로직을 구현합니다.
        Token token = tokenRepository.save(
                Token.builder()
                        .id(userId)
                        .refresh_token(refreshToken)
                        .expiration(Long.valueOf(env.getProperty("refresh_token.expiration_time")))
                        .build()
        );
        return token.getRefresh_token();
    }

    public Claims getClaimsFromToken(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException ex) {
            throw new ExpiredTokenException();
        }catch (JwtException ex){
            throw new InvalidTokenException();
        }
    }

    public boolean isExpired(String token) {

        Date date = getClaimsFromToken(token).getExpiration();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); // use your desired format
        String formattedDate = sdf.format(date);
        log.info("Expired Time : {}",formattedDate);
        return false;
    }

    public String getTypeFromToken(String token) {
        return getClaimsFromToken(token).get("type", String.class);
    }
    public void isValidType(String token,String tokenType){
        if(!getTypeFromToken(token).equals(tokenType)){
            throw new InvalidTokenTypeException();
        }
    }
    public String getUserIdFromToken(String token) {
        return getClaimsFromToken(token).get(USER_ID_ATTRIBUTE_KEY,String.class);
    }
    //Interceptor에서 검증.
    public boolean validRefreshToken(String userId, String refreshToken)  {
        //Redis에 해당 유저 정보 존재하지 않음-> 발급이 안됐거나, 만료된 상태.
        Token token = tokenRepository.findById(userId).orElseThrow(ExpiredTokenException::new);
        // 토큰이 같은지 비교
        if(!token.getRefresh_token().equals(refreshToken)) {
            //토큰이 다른경우
            throw new NotMatchRefreshTokenException();
        } else {
            return true;
        }
    }
    public TokenDto reGenerateAccessToken(String userId, HttpServletResponse response) {
        String refreshToken = generateRefreshToken(userId);
        /*
        Cookie cookie = new Cookie("refreshToken", newRefreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/"); // 모든 경로에서 쿠키 사용
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
         */

        ResponseCookie cookie = getResponseCookie(refreshToken);
        response.addHeader("Set-Cookie", cookie.toString());
        //액세스 토큰 재발급 시, 새로운 리프레시 토큰 포함.
        return TokenDto.builder()
                .accessToken(generateAccessToken(userId))
                .build();
    }
    public ResponseCookie getResponseCookie(String refreshToken) {
        ResponseCookie cookie=ResponseCookie.from("refreshToken", refreshToken)
                .path("/") // 모든 경로에서 쿠키 사용
                .sameSite("None")
                .secure(false)
                .httpOnly(true)
                .maxAge(7 * 24 * 60 * 60)
                .build();
        return cookie;
    }


}