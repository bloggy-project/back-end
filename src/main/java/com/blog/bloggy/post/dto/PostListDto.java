package com.blog.bloggy.post.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostListDto {
    private String title;
    private String categoryName;
    private String username;
    private LocalDateTime createdAt;
}
