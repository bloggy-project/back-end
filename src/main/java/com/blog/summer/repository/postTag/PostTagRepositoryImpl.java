package com.blog.summer.repository.postTag;

import com.blog.summer.domain.PostTag;
import com.blog.summer.domain.QPostTag;
import com.blog.summer.dto.PostTagStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.blog.summer.domain.QPost.post;
import static com.blog.summer.domain.QPostTag.postTag;
import static com.blog.summer.domain.QTag.tag;


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
