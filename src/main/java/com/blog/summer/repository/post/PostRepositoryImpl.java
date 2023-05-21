package com.blog.summer.repository.post;

import com.blog.summer.domain.Post;
import com.blog.summer.domain.QPostTag;
import com.blog.summer.domain.QTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.blog.summer.domain.QComment.comment;
import static com.blog.summer.domain.QPost.post;
import static com.blog.summer.domain.QPostTag.postTag;
import static com.blog.summer.domain.QTag.tag;
import static com.blog.summer.domain.QUserEntity.userEntity;


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
    public Optional<Post> findByIdWithPostTag(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(post)
                        .join(post.postTags,postTag).fetchJoin()
                        .where(post.id.eq(id))
                        .fetchOne()
        );
    }


}
