package com.blog.bloggy.user.dto;


import lombok.Builder;
import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String name;
    private String password;
    private String userId;
    private String thumbnail;
    public UserDto() {
    }


    private String encryptedPwd;

}
