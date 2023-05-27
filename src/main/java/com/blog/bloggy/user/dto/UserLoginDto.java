package com.blog.bloggy.user.dto;

import lombok.Data;

@Data
public class UserLoginDto {
    private String name;
    private String email;
    private String password;
}
