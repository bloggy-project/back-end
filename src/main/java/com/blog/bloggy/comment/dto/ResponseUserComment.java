package com.blog.bloggy.comment.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserComment {
    private String userId;
    private String body;
    private String name;

}
