package com.blog.summer.dto.favorite;


import lombok.Data;

@Data
public class RequestFavorite {
    Long postId;
    String userId;
}
