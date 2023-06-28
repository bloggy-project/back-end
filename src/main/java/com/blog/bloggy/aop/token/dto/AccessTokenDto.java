package com.blog.bloggy.aop.token.dto;

import lombok.Builder;
import lombok.Data;
@Data
public class AccessTokenDto {
    private String userId;

    public AccessTokenDto() {
    }

    @Builder
    public AccessTokenDto(String userId) {
        this.userId = userId;
    }
}
