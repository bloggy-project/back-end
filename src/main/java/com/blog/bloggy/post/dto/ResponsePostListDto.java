package com.blog.bloggy.post.dto;

import com.blog.bloggy.post.model.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponsePostListDto {
    private Post post;
    private long commentCount;
    private long favoriteCount;

    @Builder
    @QueryProjection
    public ResponsePostListDto(Post post, long commentCount, long favoriteCount) {
        this.post=post;
        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
    }
}
