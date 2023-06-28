package com.blog.bloggy.postTag.model;


import com.blog.bloggy.postTag.dto.PostTagStatus;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.tag.model.Tag;
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
@Table(indexes = {
        @Index(name = "fk_tag_post", columnList = "post_id"),
        @Index(name = "fk_tag", columnList = "tag_id")
})
public class PostTag {
    @Id
    @GeneratedValue
    @Column(name = "post_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post tagPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Tag tag;

    private String tagName;

    @Enumerated(EnumType.STRING)
    private PostTagStatus status;

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
