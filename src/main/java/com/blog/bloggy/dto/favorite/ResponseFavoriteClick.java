package com.blog.bloggy.dto.favorite;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseFavoriteClick {
    Long postId;
    String userId;
}
