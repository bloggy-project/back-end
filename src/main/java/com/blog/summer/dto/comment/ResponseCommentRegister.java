package com.blog.summer.dto.comment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseCommentRegister {
    private Long commentId;
    private String title;
    private String body;
    private String name;

}
