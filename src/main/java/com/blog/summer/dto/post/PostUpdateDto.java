package com.blog.summer.dto.post;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostUpdateDto {
    private Long postId;
    private String title;
    private String content;
    private String userId;
    private String categoryName;
    private List<String> tagNames;
}
