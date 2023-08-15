package com.blog.bloggy.post.dto;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ResponsePost implements Serializable {
    private Long postId;

    private String thumbnail;

    private String title;

    private String subContent;

    private String content;

    private String username;

    private List<String> tagNames = new ArrayList<>();

    private boolean modified=false;

    private LocalDateTime updatedAt;

    public ResponsePost() {
    }

    public ResponsePost(Long postId, String thumbnail, String title, String subContent, String content, String username, List<String> tagNames, boolean modified, LocalDateTime updatedAt) {
        this.postId = postId;
        this.thumbnail = thumbnail;
        this.title = title;
        this.subContent=subContent;
        this.content = content;
        this.username = username;
        this.tagNames = tagNames;
        this.modified = modified;
        this.updatedAt = updatedAt;
    }
}
