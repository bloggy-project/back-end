package com.blog.bloggy.post.model;


import com.blog.bloggy.comment.model.Comment;
import com.blog.bloggy.common.model.BaseTimeEntity;
import com.blog.bloggy.favorite.model.Favorite;
import com.blog.bloggy.postTag.model.PostTag;
import com.blog.bloggy.user.model.UserEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(indexes = @Index(name = "fk_post_user",columnList = "users_id"))
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    //Redis 캐시에서는 primitive type 권장?
    private Long views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity postUser;

    @OneToMany(mappedBy = "commentPost")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "favoritePost")
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "tagPost")
    private List<PostTag> postTags = new ArrayList<>();




    public Post() {}



    public void addComment(Comment comment) {
        this.comments.add(comment);
    }


    public Post(Long id) {
        this.id = id;
    }
    /*
    username추가하여 사용안함
    @Builder
    public Post(Long id,String title, String content, UserEntity user) {
        this.id= id;
        this.views =1L;
        this.title = title;
        this.content = content;
        this.postUser = user;
    }
     */
    @Builder
    public Post(Long id,String title, String content, UserEntity user) {
        this.id= id;
        this.views =1L;
        this.title = title;
        this.content = content;
        this.postUser = user;
    }
    @Builder //updatePost 테스트용 생성자 위의 생성자랑 충돌함...
    public Post(Long id, String title, String content, List<PostTag> postTags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.postTags = postTags;
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
        if(this.postTags==null)
            this.postTags=new ArrayList<>();
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

