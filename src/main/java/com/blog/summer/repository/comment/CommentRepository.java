package com.blog.summer.repository.comment;

import com.blog.summer.domain.Comment;
import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Long>,CommentRepositoryCustom {

    List<Comment> findByCommentPost(Post post);

    /*
    @Query("select c FROM Comment c WHERE c.status = 'REGISTERED' AND c.commentUser = :user")
    List<Comment> findRegisteredByCommentUser(UserEntity user);
     */
}
