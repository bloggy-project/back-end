package com.blog.bloggy.dto.user;

import lombok.Data;

@Data
public class UserLoginDto {
    private String name;
    private String email;
    private String password;
}
