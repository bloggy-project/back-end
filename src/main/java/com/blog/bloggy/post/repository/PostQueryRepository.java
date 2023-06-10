package com.blog.bloggy.post.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


import static com.blog.bloggy.post.model.QPost.post;


@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Transactional
    public void addViewCntFromRedis(Long postId, Long views) {
        queryFactory
                .update(post)
                .set(post.views,views)
                .where(post.id.eq(postId))
                .execute();
    }

}
