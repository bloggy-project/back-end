package com.blog.bloggy.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestUserDto {
    private String email;
    private String name;
    private String thumbnail;
    private String blogName;

    public TestUserDto() {
    }

    public TestUserDto(String email, String name, String thumbnail,String blogName) {
        this.email = email;
        this.name = name;
        this.thumbnail = thumbnail;
        this.blogName = blogName;
    }
}
