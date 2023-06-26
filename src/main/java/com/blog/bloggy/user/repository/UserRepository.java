package com.blog.bloggy.user.repository;

import com.blog.bloggy.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>,UserRepositoryCustom {

    Optional<UserEntity> findById(Long usersId);
    Optional<UserEntity> findByUserId(String userId);

    Optional<UserEntity> findByEmail(String username);

    Optional<UserEntity> findByName(String name);

}
