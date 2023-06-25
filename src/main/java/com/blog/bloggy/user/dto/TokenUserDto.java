package com.blog.bloggy.user.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class TokenUserDto {
    private String userId;
    private String name;
    private String email;

    @Builder
    public TokenUserDto(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "TokenUserDto{" +
                "usersId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
