package com.blog.bloggy.comment.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponsePostComment {
    private Long postId;
    private String body;
    private String name;
}
