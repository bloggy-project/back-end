package com.blog.bloggy.comment.repository;

import com.blog.bloggy.comment.model.Comment;
import com.blog.bloggy.user.model.UserEntity;

import java.util.List;

public interface CommentRepositoryCustom {
    public void deleteCommentsByPostId(Long postId);

    List<Comment> findRegisteredByCommentUser(UserEntity user);
}
