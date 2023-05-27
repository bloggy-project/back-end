package com.blog.bloggy.user.dto;


import lombok.Data;

@Data
public class RequestLogin {

    private String email;

    private String password;

}
