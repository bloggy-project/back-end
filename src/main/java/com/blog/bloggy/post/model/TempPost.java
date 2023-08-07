package com.blog.bloggy.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@RedisHash("tempPost")
public class TempPost {
    @Id
    @JsonIgnore
    private String id;
    private String title;
    private String content;
    private List<String> tagNames = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiration;

    public TempPost() {}

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
    @Builder
    public TempPost(String id, String title, String content, List<String> tagNames, List<String> imageList, Long expiration) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tagNames = tagNames;
        this.imageList = imageList;
        this.expiration = expiration;
    }

}
