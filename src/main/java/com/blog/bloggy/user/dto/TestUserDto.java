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
    private String description;

    public TestUserDto() {
    }

    public TestUserDto(String email, String name, String thumbnail,String blogName, String description) {
        this.email = email;
        this.name = name;
        this.thumbnail = thumbnail;
        this.blogName = blogName;
        this.description=description;
    }
}
