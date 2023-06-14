package com.blog.bloggy.post.repository;

import com.blog.bloggy.post.model.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.blog.bloggy.comment.model.QComment.comment;
import static com.blog.bloggy.post.model.QPost.post;
import static com.blog.bloggy.postTag.model.QPostTag.postTag;
import static com.blog.bloggy.user.model.QUserEntity.userEntity;
import static org.springframework.util.StringUtils.hasText;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> findByIdWithUser(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(post)
                        .join(post.postUser, userEntity).fetchJoin()
                        .where(post.id.eq(id))
                        .fetchOne()
        );
    }
    @Override
    public Optional<Post> findByIdWithUserComment(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(post)
                        .join(post.postUser,userEntity).fetchJoin()
                        .join(post.comments, comment).fetchJoin()
                        .where(post.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
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
    @Override
    public Page<Post> findUserPagePostAll(String name,Pageable pageable) {
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

    @Override
    public Optional<Post> findByIdWithPostTag(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(post)
                        .join(post.postTags,postTag).fetchJoin()
                        .where(post.id.eq(id))
                        .fetchOne()
        );
    }

    private BooleanExpression usernameEq(String name) {
        return hasText(name) ? post.postUser.name.eq(name) : null;
    }

}
