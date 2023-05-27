package com.blog.bloggy.postTag.repository;

import com.blog.bloggy.postTag.model.PostTag;
import com.blog.bloggy.postTag.dto.PostTagStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.blog.bloggy.post.model.QPost.post;
import static com.blog.bloggy.postTag.model.QPostTag.postTag;
import static com.blog.bloggy.tag.model.QTag.tag;


@RequiredArgsConstructor
public class PostTagRepositoryImpl implements PostTagRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<PostTag> findByPostIdAndTagId(Long postId, Long tagId) {
        return Optional.ofNullable(
                queryFactory
                        .select(postTag)
                        .from(postTag)
                        .join(postTag.tag, tag).fetchJoin()
                        .join(postTag.tagPost, post).fetchJoin()
                        .where(
                                postTag.tagPost.id.eq(postId),
                                postTag.tag.id.eq(tagId)
                        ).fetchOne()
        );
    }

    @Override
    public List<PostTag> findByPostIdWithTagFetchJoin(Long postId) {
        return queryFactory
                .selectFrom(postTag)
                .join(postTag.tag,tag).fetchJoin()
                .where(postTag.tagPost.id.eq(postId))
                .fetch();
    }

    @Override
    public List<String> findTagNamesByStatusUpdate() {
        return queryFactory
                .selectDistinct(postTag.tagName)
                .from(postTag)
                .where(postTag.status.eq(PostTagStatus.UPDATED))
                .fetch();
    }

    @Override
    public List<PostTag> findAllStatusUpdate() {
        return queryFactory
                .select(postTag)
                .from(postTag)
                .where(postTag.status.eq(PostTagStatus.UPDATED))
                .fetch();
    }

    @Override
    @Transactional
    public void deleteAllStatusDelete(){
        queryFactory
                .delete(postTag)
                .where(postTag.status.eq(PostTagStatus.DELETED))
                .execute();
    }

}
