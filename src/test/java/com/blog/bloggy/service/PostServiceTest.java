package com.blog.bloggy.service;

import com.blog.bloggy.domain.Post;
import com.blog.bloggy.dto.comment.CommentDto;
import com.blog.bloggy.dto.comment.ResponseCommentRegister;
import com.blog.bloggy.dto.post.PostDto;
import com.blog.bloggy.dto.post.ResponsePostRegister;
import com.blog.bloggy.exception.NotFoundException;
import com.blog.bloggy.repository.comment.CommentRepository;
import com.blog.bloggy.repository.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Test
    @Transactional
    void createPost() {
        ResponsePostRegister responsePost = createAndGetResponsePostRegister();
        Optional<Post> postOpt = postRepository.findById(responsePost.getPostId());
        Post post = postOpt.orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        Long postId = post.getId();
        assertEquals(responsePost.getPostId(), postId);
        assertEquals(responsePost.getTitle(), post.getTitle());
        assertEquals(responsePost.getContent(), post.getContent());
        assertEquals(responsePost.getCategoryName(),post.getCategoryName());
    }

    private ResponsePostRegister createAndGetResponsePostRegister() {
        PostDto postDto=PostDto.builder()
                .title("테스트제목")
                .content("테스트내용")
                .userId("c7e2f268-29a3-4fc8-9a9a-517392e018a6")
                .categoryName("테스트카테고리")
                .build();
        ResponsePostRegister responsePost = postService.createPost(postDto);
        return responsePost;
    }
    @Test
    @Transactional
    void deletePost() {
        ResponsePostRegister responsePost = createAndGetResponsePostRegister();
        Optional<Post> postOpt = postRepository.findById(responsePost.getPostId());
        Post post = postOpt.orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        Long postId = post.getId();

        postService.deletePostAndMark(postId);
        assertEquals(Optional.empty(),postRepository.findById(postId));

    }



    private ResponseCommentRegister leaveComment(Long postId,String body) {
         return commentService.createComment(CommentDto.builder()
                .postId(postId)
                .body(body)
                .build());
    }


}