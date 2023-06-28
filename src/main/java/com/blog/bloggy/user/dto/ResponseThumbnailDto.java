package com.blog.bloggy.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseThumbnailDto {
    private String thumbnail;
    private String name;
}

