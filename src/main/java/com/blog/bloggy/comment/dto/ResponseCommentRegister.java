package com.blog.bloggy.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseCommentRegister {
    private Long commentId;
    private String title;
    private String body;
    private String name;

}
