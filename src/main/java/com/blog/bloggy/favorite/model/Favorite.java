package com.blog.bloggy.favorite.model;


import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.user.model.UserEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "fk_favorite_post", columnList = "post_id"),
        @Index(name = "fk_favorite_user", columnList = "users_id")
})
public class Favorite {
    @Id
    @GeneratedValue
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post favoritePost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity favoriteUser;


    public void setAddFavorite(Post post) {
        this.favoritePost=post;
        this.favoritePost.addFavorite(this);
    }

    public void setRegisterFavorite(Post post, UserEntity user) {
        this.favoritePost=post;
        this.favoritePost.addFavorite(this);
        this.favoriteUser=user;
    }
}


