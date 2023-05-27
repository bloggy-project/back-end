package com.blog.bloggy.user.dto;


import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;


    private String encryptedPwd;

}
