package com.blog.bloggy.repository.comment;

import com.blog.bloggy.domain.Comment;
import com.blog.bloggy.domain.UserEntity;

import java.util.List;

public interface CommentRepositoryCustom {
    public void deleteCommentsByPostId(Long postId);

    List<Comment> findRegisteredByCommentUser(UserEntity user);
}
