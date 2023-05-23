package com.blog.bloggy.common.util;

import com.blog.bloggy.common.exception.*;
import com.blog.bloggy.domain.Token;
import com.blog.bloggy.dto.TokenDto;
import com.blog.bloggy.repository.TokenRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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

    private static final String ACCESS_TOKEN_TYPE="ACCESS";
    private static final String REFRESH_TOKEN_TYPE="REFRESH";


    public String generateToken(String userId,String type){
        // 1. token 내부에 저장할 정보
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("type",type);
        // 2. token 생성일
        Date createdDate = new Date(); //현재 시간으로 생성.
        // 3. token 만료일
        Date expirationDate = createdDate;
        Calendar cal=Calendar.getInstance();
        cal.setTime(expirationDate);
        if(type.equals(ACCESS_TOKEN_TYPE)){
            cal.add(Calendar.SECOND, Integer.parseInt(env.getProperty("token.expiration_time")));
            expirationDate = cal.getTime();
            return Jwts.builder()
                    .setClaims(claims)      // 1
                    .setIssuedAt(createdDate)       // 2
                    .setExpiration(expirationDate)      // 3
                    .signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                    .compact();
        }
        else if(type.equals(REFRESH_TOKEN_TYPE)){
            cal.add(Calendar.SECOND, Integer.parseInt(env.getProperty("refresh_token.random_time")));
            expirationDate = cal.getTime();
            return Jwts.builder()
                    .setClaims(claims)      // 1
                    .setIssuedAt(createdDate)       // 2
                    .setExpiration(expirationDate)      // 3
                    .signWith(SignatureAlgorithm.HS512,env.getProperty("refresh_token.secret"))
                    .compact();
        }
        throw new InvalidTokenTypeException(type);
    }
    public String generateAccessToken(String userId) {
        return generateToken(userId,ACCESS_TOKEN_TYPE);
    }


    public Claims getClaimsFromAccessToken(String token) {
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
    public Claims getClaimsFromRefreshToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(env.getProperty("refresh_token.secret"))
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException ex) {
            throw new ExpiredTokenException();
        }catch (JwtException ex){
            throw new InvalidTokenException();
        }
    }

    public boolean isExpired(String token) {

        Date date = getClaimsFromAccessToken(token).getExpiration();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); // use your desired format
        String formattedDate = sdf.format(date);
        log.info("Expired Time : {}",formattedDate);
        return false;
    }
    public String getUserIdFromAccessToken(String token) {
        return getClaimsFromAccessToken(token).get(USER_ID_ATTRIBUTE_KEY,String.class);
    }
    public String getUserIdFromRefreshToken(String token) {
        return getClaimsFromRefreshToken(token).get(USER_ID_ATTRIBUTE_KEY,String.class);
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
    public TokenDto reGenerateAccessToken(String userId) {
        //액세스 토큰 재발급 시, 새로운 리프레시 토큰 포함.
        return TokenDto.builder()
                .access_token(generateAccessToken(userId))
                .refresh_token(generateRefreshToken(userId))
                .build();
    }
    // Refresh Token을 발급하는 메서드
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

}