package com.blog.bloggy.dto.post;

import com.blog.bloggy.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostDto extends BaseTimeEntity {
    private String title;
    private String content;
    private String userId;
    private String categoryName;
    private List<String> tagNames;
}
