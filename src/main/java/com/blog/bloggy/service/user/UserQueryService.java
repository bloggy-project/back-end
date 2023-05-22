package com.blog.bloggy.service.user;


import com.blog.bloggy.domain.Comment;
import com.blog.bloggy.domain.UserEntity;
import com.blog.bloggy.dto.comment.ResponseUserComment;
import com.blog.bloggy.exception.NotFoundException;
import com.blog.bloggy.repository.comment.CommentRepository;
import com.blog.bloggy.repository.UserRepository;
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
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
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
