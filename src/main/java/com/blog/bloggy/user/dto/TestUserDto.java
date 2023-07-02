package com.blog.bloggy.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestUserDto {
    private String email;
    private String name;
    private String thumbnail;

    public TestUserDto() {
    }

    public TestUserDto(String email, String name, String thumbnail) {
        this.email = email;
        this.name = name;
        this.thumbnail = thumbnail;
    }
}
