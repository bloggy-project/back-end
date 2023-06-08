package com.blog.bloggy.comment.dto;

import lombok.Data;

@Data
public class RequestRecommentRegister {

    private Long postId;
    private Long commentId;
    private String body;

}
