package com.blog.bloggy.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecommentDto {

    private Long postId;
    private Long commentId;
    private String userId;
    private String body;
}
