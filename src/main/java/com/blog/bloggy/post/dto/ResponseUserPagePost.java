package com.blog.bloggy.post.dto;

import com.blog.bloggy.postTag.model.PostTag;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseUserPagePost {
    private Long postId;

    private String title;

    private String subContent;

    private LocalDateTime createdAt;

    private List<String> tagNames;
    @Builder
    public ResponseUserPagePost(Long postId, String title, String subContent, LocalDateTime createdAt, List<String> tagNames) {
        this.postId = postId;
        this.title = title;
        this.subContent = subContent;
        this.createdAt = createdAt;
        this.tagNames = tagNames;
    }
}
