package com.blog.summer.repository.favorite;


import com.blog.summer.domain.Favorite;
import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>,FavoriteRepositoryCustom {

    Optional<Favorite> findByFavoritePostAndFavoriteUser(Post post, UserEntity user);


}
