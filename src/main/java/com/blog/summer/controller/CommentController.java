package com.blog.summer.controller;


import com.blog.summer.dto.comment.CommentDto;
import com.blog.summer.dto.comment.RequestCommentRegister;
import com.blog.summer.dto.comment.ResponseCommentRegister;
import com.blog.summer.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.blog.summer.common.util.TokenUtil.USER_ID_ATTRIBUTE_KEY;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<ResponseCommentRegister> commentRegister(
            @RequestAttribute(USER_ID_ATTRIBUTE_KEY) String userId,
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
