package com.blog.bloggy.post.dto;


import com.blog.bloggy.common.model.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponsePostRegister extends BaseTimeEntity {

    private String thumbnail;

    private Long postId;

    private String title;

    private String content;

    private List<String> tagNames;

    public ResponsePostRegister(String thumbnail, Long postId, String title, String content, List<String> tagNames) {
        this.thumbnail = thumbnail;
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.tagNames = tagNames;
    }


}
