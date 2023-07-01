package com.blog.bloggy.post.dto;


import com.blog.bloggy.common.model.BaseTimeEntity;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ResponsePost extends BaseTimeEntity  implements Serializable {
    private Long postId;

    private String thumbnail;

    private String title;

    private String content;

    private String name;

    private List<String> tagNames = new ArrayList<>();

    private boolean modified=false;

    public ResponsePost() {
    }

    public ResponsePost(Long postId, String thumbnail, String title, String content, String name, List<String> tagNames) {
        this.postId = postId;
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;
        this.name = name;
        this.tagNames = tagNames;
    }

    public ResponsePost(Long postId, String thumbnail, String title, String content, String name, List<String> tagNames, boolean modified) {
        this.postId = postId;
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;
        this.name = name;
        this.tagNames = tagNames;
        this.modified = modified;
    }

}
