package com.blog.bloggy.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestThumbnailDto {
    private String thumbnail;
    private String name;

    public RequestThumbnailDto() {
    }

    public RequestThumbnailDto(String thumbnail, String name) {
        this.thumbnail = thumbnail;
        this.name = name;
    }
}

