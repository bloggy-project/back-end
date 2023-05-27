package com.blog.bloggy.favorite.dto;


import lombok.Data;

@Data
public class RequestFavorite {
    Long postId;
    String userId;
}
