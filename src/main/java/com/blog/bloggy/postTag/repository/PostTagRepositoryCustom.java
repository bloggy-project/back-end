package com.blog.bloggy.postTag.repository;

import com.blog.bloggy.postTag.model.PostTag;

import java.util.List;
import java.util.Optional;

public interface PostTagRepositoryCustom {

    Optional<PostTag> findByPostIdAndTagId(Long postId, Long tagId);


    List<PostTag> findByPostIdWithTagFetchJoin(Long postId);

    List<String> findTagNamesByStatusUpdate();

    List<PostTag> findAllStatusUpdate();

    void deleteAllStatusDelete();
}
