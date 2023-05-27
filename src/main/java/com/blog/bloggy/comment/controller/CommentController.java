package com.blog.bloggy.comment.controller;


import com.blog.bloggy.comment.dto.CommentDto;
import com.blog.bloggy.comment.dto.RequestCommentRegister;
import com.blog.bloggy.comment.dto.ResponseCommentRegister;
import com.blog.bloggy.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<ResponseCommentRegister> commentRegister(
            @RequestAttribute String userId,
            @RequestBody RequestCommentRegister commentRegister){
        CommentDto commentDto= CommentDto.builder()
                .postId(commentRegister.getPostId())
                .userId(userId)
                .body(commentRegister.getBody())
                .build();
        ResponseCommentRegister responseComment = commentService.createComment(commentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseComment);

    }
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deletePost(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }



}
