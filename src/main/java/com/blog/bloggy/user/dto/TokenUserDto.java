package com.blog.bloggy.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenUserDto {
    private Long usersId;
    private String name;
    private String email;

    @Override
    public String toString() {
        return "TokenUserDto{" +
                "usersId=" + usersId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Builder
    public TokenUserDto(Long usersId, String name, String email) {
        this.usersId = usersId;
        this.name = name;
        this.email = email;
    }
}
