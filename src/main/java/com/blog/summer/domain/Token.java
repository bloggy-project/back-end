package com.blog.summer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.util.concurrent.TimeUnit;

@Getter
@RedisHash("refreshToken")
public class Token {

    @Id
    @JsonIgnore
    private String id;

    private String refresh_token;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiration;

    public Token() {

    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    @Builder
    public Token(String id, String refresh_token, Long expiration) {
        this.id = id;
        this.refresh_token = refresh_token;
        this.expiration = expiration;
    }
}