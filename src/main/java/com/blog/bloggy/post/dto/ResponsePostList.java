package com.blog.bloggy.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponsePostList {
    private Long postId;
    private String thumbnail;

    private String title;
    private String subContent;
    private String username;
    private LocalDateTime createdAt;
    private long commentCount;
    private long favoriteCount;

    @Builder
    @QueryProjection
    public ResponsePostList(Long postId, String thumbnail, String title, String subContent, String username, LocalDateTime createdAt, long commentCount, long favoriteCount) {
        this.postId = postId;
        this.thumbnail = thumbnail;
        this.title = title;
        this.subContent=subContent;
        this.username = username;
        this.createdAt = createdAt;
        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
    }
}
