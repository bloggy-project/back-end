package com.blog.bloggy.post.redisRepository;

import com.blog.bloggy.post.model.TempPost;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TempPostRepository extends CrudRepository<TempPost, String> {
    Optional<TempPost> findById(String id);

    @Override
    void deleteById(String id);
}
