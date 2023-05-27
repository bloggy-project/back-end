package com.blog.bloggy.postTag.repository;

import com.blog.bloggy.postTag.model.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostTagRepository extends JpaRepository<PostTag,Long>,PostTagRepositoryCustom {

}
