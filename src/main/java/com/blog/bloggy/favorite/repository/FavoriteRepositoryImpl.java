package com.blog.bloggy.favorite.repository;


import com.blog.bloggy.favorite.model.Favorite;
import com.blog.bloggy.post.model.QPost;
import com.blog.bloggy.user.model.QUserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

import java.util.List;

import static com.blog.bloggy.favorite.model.QFavorite.favorite;
import static com.blog.bloggy.post.model.QPost.post;
import static com.blog.bloggy.user.model.QUserEntity.userEntity;


@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Transactional
    @Override
    public void deleteFavoritesByPostId(Long postId) {
        /*
        Query query = em.createNativeQuery("DELETE FROM favorite WHERE post_id = :postId");
        query.setParameter("postId", postId);
        query.executeUpdate();
        */
        queryFactory
                .delete(favorite)
                .where(favorite.favoritePost.id.eq(postId))
                .execute();
    }
    @Override
    public List<String> getPostsFavoritesUsernames(Long postId){
        //Non-Unique Key Lookup
        List<Long> ids = queryFactory
                .select(favorite.id)
                .from(favorite)
                .where(favorite.favoritePost.id.eq(postId)).fetch();

        List<String> favoriteUsernames = queryFactory
                .select(favorite.favoriteUser.name)
                .from(favorite)
                .join(favorite.favoriteUser, userEntity) //favorite.user_id = userEntity.users_id Unique Key Lookup
                .where(
                        favorite.id.in(ids) //Index Range Scan
                )
                .fetch();
        return favoriteUsernames;
    }

}
