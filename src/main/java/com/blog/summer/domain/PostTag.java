package com.blog.summer.domain;


import com.blog.summer.dto.PostTagStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostTag {
    @Id
    @GeneratedValue
    @Column(name = "post_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post tagPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    private String tagName;

    @Enumerated(EnumType.STRING)
    private PostTagStatus status;


    public static PostTag createPostTag(Post tagPost , Tag tag, String tagName, PostTagStatus status) {
        PostTag postTag = PostTag.builder()
                .tagPost(tagPost)
                .tag(tag)
                .tagName(tagName)
                .status(status)
                .build();
        tagPost.addPostTag(postTag);
        tag.addPostTag(postTag);
        return postTag;
    }

    public static PostTag updatePostTag(Post tagPost, String tagName, PostTagStatus status) {
        PostTag postTag=PostTag.builder()
                .tagName(tagName)
                .tagPost(tagPost)
                .status(status)
                .build();

        tagPost.addPostTag(postTag);
        return postTag;
    }

    public void setPost(Post tagPost) {
        this.tagPost=tagPost;
    }

    public void setStatus(PostTagStatus postTagStatus) {
        status=postTagStatus;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "PostTag{" +
                "id=" + id +
                ", tagName='" + tagName + '\'' +
                ", status=" + status +
                '}';
    }
}
