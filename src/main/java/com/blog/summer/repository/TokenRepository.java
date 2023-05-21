package com.blog.summer.repository;

import com.blog.summer.domain.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token,String> {
    Optional<Token> findById(String id);

}
