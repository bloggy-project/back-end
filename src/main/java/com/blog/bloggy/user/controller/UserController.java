package com.blog.bloggy.user.controller;


import com.blog.bloggy.aop.token.AccessTokenRequired;
import com.blog.bloggy.aop.token.dto.AccessTokenDto;
import com.blog.bloggy.user.dto.*;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.comment.dto.ResponseUserComment;
import com.blog.bloggy.user.service.UserQueryService;
import com.blog.bloggy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class UserController {
    ModelMapper mapper = new ModelMapper();
    private final UserService userService;
    private final UserQueryService userQueryService;

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){
        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);
        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
    @AccessTokenRequired
    @PatchMapping ("/users")
    public ResponseEntity<ResponseUpdateUser> updateUser(AccessTokenDto accessTokenDto,@RequestBody UpdateUser user){
        ResponseUpdateUser response = userService.updateUser(accessTokenDto,user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        Iterable<UserEntity> userList = userService.getUserByAll();
        List<ResponseUser> result = new ArrayList<>();
        userList.forEach( v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userId){
        UserDto userDto = userService.getUserByUserId(userId);

        ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @GetMapping("/users/comments/{userId}")
    public ResponseEntity<List<ResponseUserComment> > getComment(@PathVariable String userId){
        List<ResponseUserComment> registeredComment = userQueryService.getRegisteredComment(userId);
        return ResponseEntity.status(HttpStatus.OK).body(registeredComment);
    }

    @GetMapping("/users/check/{name}")
    public ResponseEntity<String> checkName(@PathVariable String name){
        String s = userService.checkValidUsername(name);
        return ResponseEntity.status(HttpStatus.OK).body(s);
    }

    @AccessTokenRequired
    @PostMapping("/thumbnail")
    public ResponseEntity<ResponseThumbnailDto> updateThumbnail(
            AccessTokenDto accessTokenDto,
            @RequestBody RequestThumbnailDto requestThumbnailDto){
        ResponseThumbnailDto response =
                userService.updateThumbnail(accessTokenDto,requestThumbnailDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
