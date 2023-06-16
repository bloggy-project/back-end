package com.blog.bloggy.post.dto;


import com.blog.bloggy.common.model.BaseTimeEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponsePostOne extends BaseTimeEntity {
    private Long postId;

    private String title;

    private String content;

    private String name;

    private boolean isFavorite;

    public ResponsePostOne(Long postId, String title, String content, String name,boolean isFavorite) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.name = name;
        this.isFavorite=isFavorite;
    }

}
