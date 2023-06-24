package com.blog.bloggy.post.dto;

import com.blog.bloggy.common.model.BaseTimeEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostDto extends BaseTimeEntity {
    private String title;
    private String content;
    private Long usersId;
    private List<String> tagNames;
}
