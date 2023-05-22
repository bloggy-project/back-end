package com.blog.bloggy.dto.comment;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponsePostComment {
    private Long postId;
    private String body;
    private String name;
}
