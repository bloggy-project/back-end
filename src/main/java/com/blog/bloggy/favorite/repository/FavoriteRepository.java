package com.blog.bloggy.favorite.repository;


import com.blog.bloggy.favorite.model.Favorite;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>,FavoriteRepositoryCustom {

    Optional<Favorite> findByFavoritePostAndFavoriteUser(Post post, UserEntity user);


}
