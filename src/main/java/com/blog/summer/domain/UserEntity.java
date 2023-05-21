package com.blog.summer.domain;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
public class UserEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    private String email;

    private String name;

    private String userId;

    private String encryptedPwd;

    @OneToMany(mappedBy = "postUser")
    private List<Post> posts=new ArrayList<>();

    @OneToMany(mappedBy = "commentUser")
    private List<Comment> comments=new ArrayList<>();

    public UserEntity() {}

    @Builder
    public UserEntity(String email, String name, String userId) {
        this.email = email;
        this.name = name;
        this.userId = userId;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void setEncryptedPwd(String encode) {
        this.encryptedPwd=encode;
    }



}
