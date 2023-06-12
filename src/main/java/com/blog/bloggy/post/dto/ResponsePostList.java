package com.blog.bloggy.post.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponsePostList {
    private Long postId;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private Long commentCount;
    private Long favoriteCount;
}
