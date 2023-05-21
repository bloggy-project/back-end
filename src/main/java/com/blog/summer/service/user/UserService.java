package com.blog.summer.service.user;

import com.blog.summer.domain.Token;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.user.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);

    Iterable<UserEntity> getUserByAll();

    UserDto getUserDetailsByEmail(String username);


}
