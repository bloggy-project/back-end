package com.blog.bloggy.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponsePostList {
    private Long postId;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private Long commentCount;
    private Long favoriteCount;
    @Builder
    @QueryProjection
    public ResponsePostList(Long postId, String title, String content, String username, LocalDateTime createdAt, Long commentCount, Long favoriteCount) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.username = username;
        this.createdAt = createdAt;
        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
    }
}