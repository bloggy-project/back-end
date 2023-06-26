package com.blog.bloggy.post.repository;

import com.blog.bloggy.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepositoryCustom {

    Optional<Post> findByIdWithUser(Long id);

    Optional<Post> findByIdWithUserComment(Long id);


    Optional<Post> findByIdWithPostTag(Long id);


}
