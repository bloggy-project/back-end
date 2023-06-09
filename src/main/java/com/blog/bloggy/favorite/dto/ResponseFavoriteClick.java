package com.blog.bloggy.favorite.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseFavoriteClick {
    Long postId;

    String username;

    public ResponseFavoriteClick(Long postId, String username) {
        this.postId = postId;
        this.username = username;
    }
}
