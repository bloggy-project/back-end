package com.blog.summer.repository.favorite;


import com.blog.summer.domain.QFavorite;
import com.blog.summer.domain.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static com.blog.summer.domain.QFavorite.favorite;


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
