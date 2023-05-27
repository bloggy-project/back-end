package com.blog.bloggy.token.repository;

import com.blog.bloggy.token.model.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token,String> {
    Optional<Token> findById(String id);
}
