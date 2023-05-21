package com.blog.summer.dto.post;


import com.blog.summer.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponsePostRegister extends BaseTimeEntity {

    private Long postId;

    private String categoryName;

    private String title;

    private String content;

    private String userId;

    private List<String> tagNames;

    public ResponsePostRegister(Long postId, String categoryName, String title,
                                String content, String userId, List<String> tagNames) {
        this.postId = postId;
        this.categoryName = categoryName;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.tagNames = tagNames;
    }


}
