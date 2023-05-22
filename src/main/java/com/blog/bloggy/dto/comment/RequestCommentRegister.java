package com.blog.bloggy.dto.comment;

import lombok.Data;

@Data
public class RequestCommentRegister {

    private Long postId;
    private String body;

}
