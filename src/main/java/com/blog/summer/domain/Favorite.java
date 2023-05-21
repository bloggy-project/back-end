package com.blog.summer.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post favoritePost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
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


