package com.blog.bloggy.user.repository;

import com.blog.bloggy.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepositoryCustom  {

    Optional<Long> findIdByName(String name);
}
