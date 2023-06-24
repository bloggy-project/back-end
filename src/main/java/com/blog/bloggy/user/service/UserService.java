package com.blog.bloggy.user.service;

import com.blog.bloggy.user.dto.TokenUserDto;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);

    Iterable<UserEntity> getUserByAll();

    UserDto getUserDetailsByEmail(String username);

    TokenUserDto getTokenUserDtoByUserId(String userId);
}
