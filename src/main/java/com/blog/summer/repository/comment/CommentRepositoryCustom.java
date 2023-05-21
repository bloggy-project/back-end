package com.blog.summer.repository.comment;

import com.blog.summer.domain.Comment;
import com.blog.summer.domain.UserEntity;

import java.util.List;

public interface CommentRepositoryCustom {
    public void deleteCommentsByPostId(Long postId);

    List<Comment> findRegisteredByCommentUser(UserEntity user);
}
