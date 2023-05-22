package com.blog.bloggy.controller;


import com.blog.bloggy.domain.badcomment.BadComment;
import com.blog.bloggy.domain.badcomment.RequestBadComment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BadCommentController {

    @PostMapping("/badComment")
    public BadComment badCommentResponse(@RequestBody RequestBadComment badCommentDto){

        BadComment badComment= new BadComment();
        badComment.setBody(badCommentDto.getBody());

        return badComment;

    }
    @PostMapping("/entity/badComment")
    public ResponseEntity<BadComment> badCommentResponseEntity(@RequestBody RequestBadComment badCommentDto){

        BadComment badComment= new BadComment();
        badComment.setBody(badCommentDto.getBody());

        return ResponseEntity.status(HttpStatus.CREATED).body(badComment);

    }
}
