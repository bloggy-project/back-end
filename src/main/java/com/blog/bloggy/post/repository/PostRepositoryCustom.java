package com.blog.bloggy.post.repository;

import com.blog.bloggy.post.model.Post;

import java.util.Optional;

public interface PostRepositoryCustom {

    Optional<Post> findByIdWithUser(Long id);

    Optional<Post> findByIdWithUserComment(Long id);


}
