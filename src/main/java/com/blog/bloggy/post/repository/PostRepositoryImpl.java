package com.blog.bloggy.post.repository;

import com.blog.bloggy.post.model.Post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

import static com.blog.bloggy.comment.model.QComment.comment;
import static com.blog.bloggy.post.model.QPost.post;
import static com.blog.bloggy.postTag.model.QPostTag.postTag;
import static com.blog.bloggy.user.model.QUserEntity.userEntity;

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



}
