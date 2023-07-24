package com.blog.bloggy.user.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUpdateUser {
    private String email;
    private String name;
    private String thumbnail;
    private String blogName;
    private String description;
}
