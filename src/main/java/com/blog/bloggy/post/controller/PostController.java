package com.blog.bloggy.post.controller;


import com.blog.bloggy.aop.token.AccessTokenRequired;
import com.blog.bloggy.aop.token.Admin;
import com.blog.bloggy.aop.token.dto.AccessTokenDto;
import com.blog.bloggy.post.dto.*;
import com.blog.bloggy.post.service.PostService;
import com.blog.bloggy.user.dto.TokenUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @AccessTokenRequired
    @PostMapping("/posts")
    public ResponseEntity<ResponsePostRegister> postRegister(
            AccessTokenDto tokenDto,
            @RequestBody RequestPostRegister requestPostRegister) {

        PostDto postDto= PostDto.builder()
                .thumbnail(requestPostRegister.getThumbnail())
                .title(requestPostRegister.getTitle())
                .content(requestPostRegister.getContent())
                .userId(tokenDto.getUserId())
                .tagNames(requestPostRegister.getTagNames())
                .build();
        ResponsePostRegister responsePostRegister = postService.createPost(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responsePostRegister);
    }
    @AccessTokenRequired
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ResponsePostRegister> postUpdate(
            @PathVariable Long postId,
            @RequestBody RequestPostRegister requestPostRegister) {

        PostUpdateDto postDto= PostUpdateDto.builder()
                .thumbnail(requestPostRegister.getThumbnail())
                .postId(postId)
                .title(requestPostRegister.getTitle())
                .content(requestPostRegister.getContent())
                .tagNames(requestPostRegister.getTagNames())
                .build();
        ResponsePostRegister responsePostRegister = postService.updatePost(postDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responsePostRegister);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePostAndMarkAsComment(@PathVariable Long postId){
        postService.deletePostAndMark(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ResponsePostOne> getPostOne(
            @PathVariable Long postId,
            @RequestParam(value = "username",required = false) String username) {
        postService.addViewCntToRedis(postId);
        ResponsePostOne postOne = postService.getPostOne(postId,username);
        return ResponseEntity.status(HttpStatus.OK).body(postOne);
    }

    @GetMapping("/posts")
    public ResponseEntity<Slice<ResponsePostList>> getPostsAll(
            @RequestParam(value = "lastId", required = false)Long lastId){
        Pageable pageable = PageRequest.of(0,10);
        Slice<ResponsePostList> result=postService.getPosts(lastId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/posts/{name}")
    public ResponseEntity<Page<ResponseUserPagePost>> getUserPostsOrderByCreatedAt(
            @PathVariable(value = "name") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ResponseUserPagePost> results = postService.getUserPostsOrderByCreatedAt(name, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }


}
