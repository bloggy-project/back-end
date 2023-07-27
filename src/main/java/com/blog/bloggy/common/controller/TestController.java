package com.blog.bloggy.common.controller;

import com.blog.bloggy.aop.token.AccessTokenRequired;
import com.blog.bloggy.aop.token.dto.AccessTokenDto;
import com.blog.bloggy.common.exception.UserNotFoundException;
import com.blog.bloggy.user.dto.TestUserDto;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final UserRepository userRepository;
    @GetMapping(value = "/health-check")
    @ResponseBody
    public String health_check() {
        return "connect-ok";
    }

    @AccessTokenRequired
    @GetMapping(value = "/userinfo")
    public TestUserDto test(AccessTokenDto accessTokenDto){
        UserEntity user = userRepository.findByUserId(accessTokenDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException());
        TestUserDto response= TestUserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .thumbnail(user.getThumbnail())
                .blogName(user.getBlogName())
                .description(user.getDescription())
                .build();
        return response;
    }
}
