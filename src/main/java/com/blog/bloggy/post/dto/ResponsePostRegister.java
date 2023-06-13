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

    private Long postId;


    private String title;

    private String content;

    private String userId;

    private List<String> tagNames;

    public ResponsePostRegister(Long postId, String title,
                                String content, String userId, List<String> tagNames) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.tagNames = tagNames;
    }


}
