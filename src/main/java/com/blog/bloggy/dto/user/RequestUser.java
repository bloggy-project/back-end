package com.blog.bloggy.dto.user;

import lombok.Data;

@Data
public class RequestUser {

    private String email;

    private String name;

    private String pwd;
}
