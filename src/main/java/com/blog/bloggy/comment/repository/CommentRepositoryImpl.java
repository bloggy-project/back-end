package com.blog.bloggy.comment.repository;


import com.blog.bloggy.comment.model.Comment;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.comment.dto.CommentStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

import java.util.List;

import static com.blog.bloggy.comment.model.QComment.comment;


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
