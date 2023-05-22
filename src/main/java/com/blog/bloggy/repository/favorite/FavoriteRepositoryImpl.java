package com.blog.bloggy.repository.favorite;


import com.blog.bloggy.domain.QFavorite;
import com.blog.bloggy.domain.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static com.blog.bloggy.domain.QFavorite.favorite;


@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom{

    private final EntityManager em;
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
}
