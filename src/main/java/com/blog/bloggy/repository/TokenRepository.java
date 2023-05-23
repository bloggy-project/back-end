package com.blog.bloggy.repository;

import com.blog.bloggy.domain.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token,String> {
    Optional<Token> findById(String id);
}
