package com.blog.summer.dto.favorite;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseFavoriteClick {
    Long postId;
    String userId;
}
