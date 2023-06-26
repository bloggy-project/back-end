package com.blog.bloggy.user.repository;

import com.blog.bloggy.user.model.QUserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.blog.bloggy.user.model.QUserEntity.*;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> findIdByName(String name) {
        return Optional.ofNullable(
                queryFactory.select(userEntity.id)
                .from(userEntity)
                .where(userEntity.name.eq(name)).fetchOne()
        );
    }
}
