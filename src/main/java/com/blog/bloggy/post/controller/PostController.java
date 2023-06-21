package com.blog.bloggy.post.controller;


import com.blog.bloggy.post.dto.*;
import com.blog.bloggy.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.blog.bloggy.common.util.TokenUtil.USER_ID_ATTRIBUTE_KEY;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    @PostMapping("/posts")
    public ResponseEntity<ResponsePostRegister> postRegister(
            @RequestAttribute(USER_ID_ATTRIBUTE_KEY) String userId,
            @RequestBody RequestPostRegister requestPostRegister) {

        PostDto postDto= PostDto.builder()
                .title(requestPostRegister.getTitle())
                .content(requestPostRegister.getContent())
                .userId(userId)
                .tagNames(requestPostRegister.getTagNames())
                .build();
        ResponsePostRegister responsePostRegister = postService.createPost(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responsePostRegister);
    }
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ResponsePostRegister> postUpdate(
            @PathVariable Long postId,
            @RequestBody RequestPostRegister requestPostRegister) {

        PostUpdateDto postDto= PostUpdateDto.builder()
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
    public ResponseEntity<Slice<ResponsePostList>> getPostsALl(
            @RequestParam(value = "postId")Long postId){
        Pageable pageable = PageRequest.of(0,10);
        Slice<ResponsePostList> result=postService.getPosts(postId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @GetMapping("/posts/{name}")
    public ResponseEntity<Page<ResponseUserPagePostWithPostTags>> getUserPostsOrderByCreatedAt(
            @PathVariable(value = "name") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ResponseUserPagePostWithPostTags> result = postService.getUserPostsOrderByCreatedAt(name, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
