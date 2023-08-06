package com.blog.bloggy.post.controller;


import com.blog.bloggy.aop.token.AccessTokenRequired;
import com.blog.bloggy.aop.token.dto.AccessTokenDto;
import com.blog.bloggy.common.dto.TrendSearchCondition;
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

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private static final int pageSize=15;

    @AccessTokenRequired
    @PostMapping("/posts")
    public ResponseEntity<ResponsePost> postRegister(
            AccessTokenDto tokenDto,
            @RequestBody RequestPostRegister requestPostRegister) {

        PostDto postDto= PostDto.builder()
                .thumbnail(requestPostRegister.getThumbnail())
                .title(requestPostRegister.getTitle())
                .subContent(requestPostRegister.getSubContent())
                .content(requestPostRegister.getContent())
                .userId(tokenDto.getUserId())
                .tagNames(requestPostRegister.getTagNames())
                .build();
        ResponsePost response = postService.createPost(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @AccessTokenRequired
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ResponsePost> postUpdate(
            @PathVariable Long postId,
            AccessTokenDto tokenDto,
            @RequestBody RequestPostRegister requestPostRegister) {

        PostUpdateDto postDto= PostUpdateDto.builder()
                .thumbnail(requestPostRegister.getThumbnail())
                .postId(postId)
                .title(requestPostRegister.getTitle())
                .content(requestPostRegister.getContent())
                .tagNames(requestPostRegister.getTagNames())
                .userId(tokenDto.getUserId())
                .build();
        ResponsePost response = postService.updatePost(postDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePostAndMarkAsComment(@PathVariable Long postId){
        postService.deletePostAndMark(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ResponsePost> getPostOne(
            @PathVariable Long postId,
            @RequestParam(value = "username",required = false) String username) {
        //postService.addViewCntToRedis(postId); 조회수 기능 필요한지 의문
        //ResponsePostOne postOne = postService.getPostOne(postId,username);
        ResponsePost postOne = postService.getPostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postOne);
    }

    @GetMapping("/posts")
    public ResponseEntity<Slice<ResponsePostList>> getPostsAll(
            @RequestParam(value = "lastId", required = false)Long lastId){
        Pageable pageable = PageRequest.of(0,pageSize);
        Slice<ResponsePostList> result=postService.getPosts(lastId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @GetMapping("/posts/popular")
    public ResponseEntity<Slice<ResponsePostList>> getPostsAllOrderByTrend(
            @RequestParam(value = "lastId", required = false)Long lastId,
            @RequestParam(value = "date", required = false)String date,
            @RequestParam(value = "favorCount", required = false)Long favorCount
    ){
        Pageable pageable = PageRequest.of(0,pageSize);
        TrendSearchCondition condition = TrendSearchCondition.builder()
                .lastId(lastId)
                .date(date)
                .favorCount(favorCount)
                .build();
        Slice<ResponsePostList> result=postService.getPostsOrderByTrend(condition, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/posts/users/{name}")
    public ResponseEntity<Page<ResponseUserPagePost>> getUserPostsOrderByCreatedAt(
            @PathVariable(value = "name") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ResponseUserPagePost> results = postService.getUserPostsOrderByCreatedAt(name, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }


}
