package com.blog.bloggy.user.service;

import com.blog.bloggy.aop.token.dto.AccessTokenDto;
import com.blog.bloggy.user.dto.*;
import com.blog.bloggy.user.model.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);

    Iterable<UserEntity> getUserByAll();

    UserDto getUserDetailsByEmail(String username);

    TokenUserDto getTokenUserDtoByUserId(String userId);

    TestMaskingDto getTestMaskingDtoByUserId(String userId);

    ResponseThumbnailDto updateThumbnail(AccessTokenDto accessTokenDto, RequestThumbnailDto requestThumbnailDto);

    String checkValidUsername(String name);

    ResponseUpdateUser updateUser(AccessTokenDto accessTokenDto,UpdateUser user);
}
