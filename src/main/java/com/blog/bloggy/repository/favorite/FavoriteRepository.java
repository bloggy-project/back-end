package com.blog.bloggy.repository.favorite;


import com.blog.bloggy.domain.Favorite;
import com.blog.bloggy.domain.Post;
import com.blog.bloggy.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>,FavoriteRepositoryCustom {

    Optional<Favorite> findByFavoritePostAndFavoriteUser(Post post, UserEntity user);


}
