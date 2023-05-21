package com.blog.summer.repository.post;


import com.blog.summer.domain.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static com.blog.summer.domain.QPost.post;


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
