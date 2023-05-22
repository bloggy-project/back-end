package com.blog.bloggy.dto.favorite;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteDto {
    Long postId;
    String userId;
}
