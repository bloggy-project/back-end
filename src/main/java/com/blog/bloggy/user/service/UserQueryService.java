package com.blog.bloggy.user.service;


import com.blog.bloggy.comment.model.Comment;
import com.blog.bloggy.common.exception.UserNotFoundException;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.comment.dto.ResponseUserComment;
import com.blog.bloggy.comment.repository.CommentRepository;
import com.blog.bloggy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public List<ResponseUserComment> getRegisteredComment(String userId){
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException());
        List<Comment> comments = commentRepository.findRegisteredByCommentUser(user);
        List<ResponseUserComment> responseUserComments = comments.stream()
                .map(c -> ResponseUserComment.builder()
                        .userId(userId)
                        .body(c.getBody())
                        .name(user.getName())
                        .build())
                .collect(toList());
        return responseUserComments;
    }
}
