package com.blog.summer.dto.post;


import com.blog.summer.domain.BaseTimeEntity;
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
