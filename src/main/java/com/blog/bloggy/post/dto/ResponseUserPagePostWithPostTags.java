package com.blog.bloggy.post.dto;

import com.blog.bloggy.postTag.model.PostTag;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseUserPagePostWithPostTags {
    private Long postId;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private List<PostTag> postTags;

    @Builder
    @QueryProjection
    public ResponseUserPagePostWithPostTags(Long postId, String title, String content, LocalDateTime createdAt, List<PostTag> postTags) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.postTags = postTags;
    }
}
