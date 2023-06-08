package com.blog.bloggy.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseRecommentRegister {
    private Long commentId;
    private Long parentId;
    private Long depth;
    private String title;
    private String body;
    private String name;

}
