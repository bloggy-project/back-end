package com.blog.bloggy.comment.model;


import com.blog.bloggy.comment.dto.CommentStatus;
import com.blog.bloggy.common.model.BaseTimeEntity;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.user.model.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private long depth=1; //default 값은 1로 시작

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post commentPost;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UserEntity commentUser;

    private Long parentId;

    private String username;

    private String body;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    public void setRegisterComment(Post post, UserEntity user, String body, String username, CommentStatus registered) {
        this.commentPost =post;
        this.commentPost.addComment(this);
        this.commentUser =user;
        this.commentUser.addComment(this);
        this.body=body;
        this.username=username;
        this.status=registered;
    }

    public void setParentId(Long parentId, long depth) {
        this.parentId=parentId;
        this.depth=depth+1;
    }
}


