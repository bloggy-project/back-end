package com.blog.bloggy.post.dto;

import com.blog.bloggy.common.model.BaseTimeEntity;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TempPostDto extends BaseTimeEntity {
    private String title;
    private String content;
    private String userId;
    private List<String> tagNames;
    private List<String> imageList;

}
