package com.blog.bloggy.post.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostUpdateDto {
    private String thumbnail;
    private Long postId;
    private String title;
    private String content;
    private String userId;
    private String name;
    private List<String> tagNames;
}
