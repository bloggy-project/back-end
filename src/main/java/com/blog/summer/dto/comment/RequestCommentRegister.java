package com.blog.summer.dto.comment;

import lombok.Data;

@Data
public class RequestCommentRegister {

    private Long postId;
    private String body;

}
