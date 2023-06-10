package com.blog.bloggy.user.dto;


import lombok.Builder;
import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    public UserDto() {
    }
    @Builder
    public UserDto(String email, String name, String pwd) {
        this.email = email;
        this.name = name;
        this.pwd = pwd;
    }

    private String encryptedPwd;

}
