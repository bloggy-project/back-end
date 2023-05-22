package com.blog.bloggy.repository.comment;


import com.blog.bloggy.domain.Comment;
import com.blog.bloggy.domain.QComment;
import com.blog.bloggy.domain.QUserEntity;
import com.blog.bloggy.domain.UserEntity;
import com.blog.bloggy.dto.comment.CommentStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

import java.util.List;

import static com.blog.bloggy.domain.QComment.comment;
import static com.blog.bloggy.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    @Transactional
    public void deleteCommentsByPostId(Long postId) {
        /*
        Query query = em.createNativeQuery("DELETE FROM comment WHERE post_id = :postId");
        query.setParameter("postId", postId);
        query.executeUpdate();
         */
        queryFactory
                .delete(comment)
                .where(comment.commentPost.id.eq(postId))
                .execute();
    }

    @Override
    public List<Comment> findRegisteredByCommentUser(UserEntity user) {
        return queryFactory
                        .selectFrom(comment)
                        .where(
                                comment.status.eq(CommentStatus.REGISTERED),
                                comment.commentUser.eq(user)
                        )
                .fetch();
    }
}
