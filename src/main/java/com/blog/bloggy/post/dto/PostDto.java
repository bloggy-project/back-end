package com.blog.bloggy.post.dto;

import com.blog.bloggy.common.model.BaseTimeEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostDto extends BaseTimeEntity {
    private String thumbnail;
    private String title;
    private String subContent;
    private String content;
    private String userId;
    private List<String> tagNames;
}
