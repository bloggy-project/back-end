package com.blog.bloggy.comment.dto;

import lombok.Data;

@Data
public class RequestCommentRegister {

    private Long postId;
    private String body;

}
