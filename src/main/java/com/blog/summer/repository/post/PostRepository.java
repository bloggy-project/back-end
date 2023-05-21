package com.blog.summer.repository.post;

import com.blog.summer.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>,PostRepositoryCustom {

    //replace querydsl
    /*
    @Query("SELECT p FROM Post p JOIN FETCH p.postUser WHERE p.id = :id")
    Optional<Post> findByIdWithUser(@Param("id") Long id);
    @Query("SELECT p FROM Post p JOIN FETCH p.postUser Join FETCH p.comments WHERE p.id = :id")
    Optional<Post> findByIdWithUserComment(@Param("id") Long id);

    @Query("SELECT p FROM Post p JOIN FETCH p.postUser")
    List<Post> findAllWithUser();


    @Query(value="select p FROM Post p Join FETCH p.postUser"
            ,countQuery = "select count(p) from Post p")
    Page<Post> findAllWithUserCountBy(Pageable pageable);
     */

}
