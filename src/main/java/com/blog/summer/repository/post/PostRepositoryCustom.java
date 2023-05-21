package com.blog.summer.repository.post;

import com.blog.summer.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepositoryCustom {

    Optional<Post> findByIdWithUser(Long id);

    Optional<Post> findByIdWithUserComment(Long id);

    Page<Post> findPostsWithUsersAsPage(Pageable pageable);

    Optional<Post> findByIdWithPostTag(Long id);


}
