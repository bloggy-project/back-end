package com.blog.bloggy.post.dto;


import com.blog.bloggy.common.model.BaseTimeEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponsePostOne extends BaseTimeEntity {
    private Long postId;

    private String categoryName;

    private String title;

    private String content;

    private String name;

    public ResponsePostOne(Long postId, String categoryName, String title, String content, String name) {
        this.postId = postId;
        this.categoryName = categoryName;
        this.title = title;
        this.content = content;
        this.name = name;
    }

}