package com.blog.bloggy.favorite.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteDto {
    Long postId;
    String userId;
}
