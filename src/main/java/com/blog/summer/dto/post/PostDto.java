package com.blog.summer.dto.post;

import com.blog.summer.domain.BaseTimeEntity;
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
