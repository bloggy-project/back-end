package com.blog.bloggy.post.model;


import com.blog.bloggy.comment.model.Comment;
import com.blog.bloggy.common.model.BaseTimeEntity;
import com.blog.bloggy.favorite.model.Favorite;
import com.blog.bloggy.postTag.model.PostTag;
import com.blog.bloggy.user.model.UserEntity;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    private String categoryName;

    //Redis 캐시에서는 primitive type 권장?
    private Long views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UserEntity postUser;

    @OneToMany(mappedBy = "commentPost")
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "favoritePost")
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "tagPost")
    private List<PostTag> postTags = new ArrayList<>();



    public Post() {}


    public void setPostUser(UserEntity postUser) {
        this.postUser = postUser;
        this.postUser.addPost(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @Builder
    public Post(String title, String content, String categoryName) {
        this.views =1L;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
    }

    public void addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
    }

    public void removeComment(Comment comment){
        this.comments.remove(comment);
    }

    public void removeFavorite(Favorite favorite) {
        favorites.remove(favorite);
    }

    public void addPostTag(PostTag postTag) {
        this.postTags.add(postTag);
    }

    public void removePostTag(PostTag postTag) {
        postTags.remove(postTag);
    }

    public void updatePost(String title, String content) {
        this.title=title;
        this.content=content;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", views=" + views +
                ", createdAt "+this.getCreatedAt()+'\''+
                '}';
    }
}

