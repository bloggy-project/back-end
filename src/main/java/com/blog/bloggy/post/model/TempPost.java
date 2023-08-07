package com.blog.bloggy.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.util.concurrent.TimeUnit;

@Getter
@RedisHash("tempPost")
public class TempPost {
    @Id
    @JsonIgnore
    private String id;
    private String title;
    private String content;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiration;

    public TempPost() {}

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
    @Builder
    public TempPost(String id, String title, String content, Long expiration) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.expiration = expiration;
    }
}
