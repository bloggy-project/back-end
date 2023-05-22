package com.blog.bloggy.dto.comment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {

    private Long postId;
    private String userId;
    private String body;
}
