package com.blog.summer.common.util;

import com.blog.summer.common.exception.ExpiredTokenException;
import com.blog.summer.common.exception.InvalidRefreshTokenException;
import com.blog.summer.common.exception.RefreshTokenRequiredException;
import com.blog.summer.domain.Token;
import com.blog.summer.dto.TokenDto;
import com.blog.summer.repository.TokenRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenUtil {

    private final Environment env;
    private final TokenRepository tokenRepository;
    public static final String USER_ID_ATTRIBUTE_KEY = "userId";
    public static final String ACCESS_TOKEN_HEADER="accessToken";
    public static final String REFRESH_TOKEN_HEADER="refreshToken";


    /*
    public String generate(String userId) {
        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis()+
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                .compact();
        return token;
    }
     */

    public String generateAccessToken(String userId) {

        // 1. token 내부에 저장할 정보
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        // 2. token 생성일
        final Date createdDate = new Date();
        // 3. token 만료일
        final Date expirationDate = new Date(createdDate.getTime()+
                Long.parseLong(env.getProperty("token.expiration_time")));

        return Jwts.builder()
                .setClaims(claims)      // 1
                .setIssuedAt(createdDate)       // 2
                .setExpiration(expirationDate)      // 3
                .signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                .compact();
    }



    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException ex) {
            throw new ExpiredTokenException();
        }catch (JwtException ex){
            throw new InvalidRefreshTokenException();
        }
    }

    public boolean isExpired(String token) {

        Date date = getClaimsFromToken(token).getExpiration();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); // use your desired format
        String formattedDate = sdf.format(date);
        log.info("Expired Time : {}",formattedDate);
        return false;
    }

    public String getUserIdFromToken(String token) {
        return getClaimsFromToken(token).get(USER_ID_ATTRIBUTE_KEY,String.class);
    }
    //Interceptor에서 검증.
    public boolean validRefreshToken(String userId, String refreshToken)  {
        //Redis에 해당 유저 정보 존재하지 않음-> 발급이 안됐거나, 만료된 상태.
        Token token = tokenRepository.findById(userId).orElseThrow(RefreshTokenRequiredException::new);
        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getRefresh_token() == null) {
            throw new RefreshTokenRequiredException();
        } else {
            // 토큰이 같은지 비교
            if(!token.getRefresh_token().equals(refreshToken)) {
                //토큰이 다른경우
                throw new InvalidRefreshTokenException();
            } else {
                return true;
            }
        }
    }
    public TokenDto reGenerateAccessToken(String userId, String refreshToken) {
        //액세스 토큰 재발급 시, 새로운 리프레시 토큰 포함.
        return TokenDto.builder()
                .access_token(generateAccessToken(userId))
                .refresh_token(generateRefreshToken(userId))
                .build();
    }

    // Refresh Token을 발급하는 메서드
    public String generateRefreshToken(String userId) {
        // 1. token 내부에 저장할 정보
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        // 2. token 생성일
        final Date createdDate = new Date();
        // 3. token 만료일 -> 시간을 넣지 않으면, 매번 똑같은 값이 생성됨...
        final Date expirationDate = new Date(createdDate.getTime()+99999999L);

        String refreshToken = Jwts.builder()
                .setClaims(claims)      // 1
                .setIssuedAt(createdDate)       // 2
                .setExpiration(expirationDate)      // 3
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();
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