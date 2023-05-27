package com.blog.bloggy.user.dto;

import lombok.Data;

@Data
public class RequestUser {

    private String email;

    private String name;

    private String pwd;
}
