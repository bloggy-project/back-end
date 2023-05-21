package com.blog.summer.repository.postTag;

import com.blog.summer.domain.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostTagRepository extends JpaRepository<PostTag,Long>,PostTagRepositoryCustom {

}
