package com.blog.bloggy.controller;


import com.blog.bloggy.common.util.TokenUtil;
import com.blog.bloggy.domain.Post;
import com.blog.bloggy.dto.post.*;
import com.blog.bloggy.repository.post.PostRepository;
import com.blog.bloggy.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.blog.bloggy.common.util.TokenUtil.USER_ID_ATTRIBUTE_KEY;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final EntityManager em;
    private final PostService postService;
    private final PostRepository postRepository;
    @PostMapping("/posts")
    public ResponseEntity<ResponsePostRegister> postRegister(
            @RequestAttribute(USER_ID_ATTRIBUTE_KEY) String userId,
            @RequestBody RequestPostRegister requestPostRegister) {

        PostDto postDto= PostDto.builder()
                .title(requestPostRegister.getTitle())
                .content(requestPostRegister.getContent())
                .userId(userId)
                .categoryName(requestPostRegister.getCategoryName())
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
                .categoryName(requestPostRegister.getCategoryName())
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
            @PathVariable Long postId) {
        postService.addViewCntToRedis(postId);
        ResponsePostOne postOne = postService.getPostOne(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postOne);
    }

    @GetMapping("/api/postAll")
    public List<PostAllDto> getPostLazyAll(){
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(PostAllDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/postList")
    public Page<PostListDto> getPostListOrderByCreatedAt(@RequestParam Integer page,
                                                         @RequestParam Integer size){
        return postService.getPostAllByCreatedAt(page, size);
    }
}
