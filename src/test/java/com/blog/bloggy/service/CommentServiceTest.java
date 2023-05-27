package com.blog.bloggy.service;

import com.blog.bloggy.comment.service.CommentService;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.comment.dto.CommentDto;
import com.blog.bloggy.comment.dto.ResponseCommentRegister;
import com.blog.bloggy.post.dto.PostDto;
import com.blog.bloggy.post.dto.ResponsePostRegister;
import com.blog.bloggy.common.exception.NotFoundException;
import com.blog.bloggy.post.service.PostService;
import com.blog.bloggy.comment.repository.CommentRepository;
import com.blog.bloggy.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;

    @Test
    @Transactional
    void deleteComment() {
        ResponsePostRegister responsePost = createAndGetResponsePostRegister();
        Optional<Post> postOpt = postRepository.findById(responsePost.getPostId());
        Post post = postOpt.orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        Long postId = post.getId();
        Long commentId1= leaveComment(postId, "댓글1").getCommentId();
        Long commentId2  = leaveComment(postId, "댓글2").getCommentId();

        commentService.deleteComment(commentId1);
       // assertEquals(postService.getCommentList(postId).size(),1);
        assertEquals(Optional.empty(),commentRepository.findById(commentId1));

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
    private ResponseCommentRegister leaveComment(Long postId, String body) {
        return commentService.createComment(CommentDto.builder()
                .postId(postId)
                .body(body)
                .build());
    }

    @Test
    @Transactional
    public void multibagExceptionCheck() throws Exception {
        //given

        //when

        //then
    }
}