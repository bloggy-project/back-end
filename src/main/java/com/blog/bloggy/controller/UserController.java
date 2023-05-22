package com.blog.bloggy.controller;


import com.blog.bloggy.domain.UserEntity;
import com.blog.bloggy.dto.comment.ResponseUserComment;
import com.blog.bloggy.dto.user.RequestUser;
import com.blog.bloggy.dto.user.ResponseUser;
import com.blog.bloggy.dto.user.UserDto;
import com.blog.bloggy.service.user.UserQueryService;
import com.blog.bloggy.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
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

}
