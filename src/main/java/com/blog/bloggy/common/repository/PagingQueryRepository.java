package com.blog.bloggy.common.repository;

import com.blog.bloggy.post.dto.ResponseUserPagePost;
import com.blog.bloggy.post.dto.ResponseUserPagePostForLazy;
import com.blog.bloggy.post.model.Post;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.blog.bloggy.post.model.QPost.post;
import static com.blog.bloggy.postTag.model.QPostTag.postTag;
import static com.blog.bloggy.user.model.QUserEntity.userEntity;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class PagingQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Post> findPostsWithUsersAsPage(Pageable pageable) {
        List<Post> posts = queryFactory
                .selectFrom(post)
                .join(post.postUser, userEntity).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .fetchOne();
        return new PageImpl<>(posts, pageable, total);
    }
    // 인덱스 미사용버전
    public Page<Post> findUserPagePostAllV1(String name, Pageable pageable) {
        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .join(post.postTags,postTag).fetchJoin()
                .leftJoin(post.postUser, userEntity)
                .where(
                        usernameEq(name)
                )
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.postUser, userEntity)
                .where(
                        usernameEq(name)
                )
                .fetchOne();
        return new PageImpl<>(posts, pageable, total);
    }

    private BooleanExpression usernameEq(String name) {
        return hasText(name) ? post.postUser.name.eq(name) : null;
    }

    // Entity를 Dto로 전환 문제 해결 x. 사용 금지
    public Page<ResponseUserPagePostForLazy> findUserPagePostAllV3(String name, Pageable pageable) {
        // 1) 커버링 인덱스로 대상 조회
        List<Long> ids = queryFactory
                .select(post.id)
                .from(post)
                .leftJoin(post.postUser, userEntity)
                .where(usernameEq(name))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<ResponseUserPagePostForLazy> posts = queryFactory
                .select(Projections.fields(ResponseUserPagePostForLazy.class,
                        post.id,
                        post.title,
                        post.content,
                        post.createdAt,
                        Projections.list(
                                post.postTags.as("postTags")
                        )
                ))
                .from(post)
                .join(post.postTags, postTag).fetchJoin()
                .where(post.id.in(ids))
                .orderBy(post.createdAt.desc())
                .fetch();
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.id.in(ids))
                .fetchOne();
        return new PageImpl<>(posts, pageable, total);
    }
}
