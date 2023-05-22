package com.blog.bloggy.service;


import com.blog.bloggy.domain.Comment;
import com.blog.bloggy.domain.Post;
import com.blog.bloggy.domain.UserEntity;
import com.blog.bloggy.dto.comment.CommentDto;
import com.blog.bloggy.dto.comment.CommentStatus;
import com.blog.bloggy.dto.comment.ResponseCommentRegister;
import com.blog.bloggy.exception.NotFoundException;
import com.blog.bloggy.repository.UserRepository;
import com.blog.bloggy.repository.comment.CommentRepository;
import com.blog.bloggy.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ResponseCommentRegister createComment(CommentDto commentDto) {
        Long postId = commentDto.getPostId();
        String body = commentDto.getBody();
        Post post = postRepository.findByIdWithUser(postId).orElseThrow(()
                -> new NotFoundException("게시글이 존재하지 않습니다."));
        Comment comment = new Comment();
        UserEntity user = userRepository.findByUserId(commentDto.getUserId()).orElseThrow(()
                -> new NotFoundException("사용자가 존재하지 않음."));

        comment.setRegisterComment(post,user,body,user.getName(), CommentStatus.REGISTERED);
        commentRepository.save(comment);
        return ResponseCommentRegister.builder()
                .commentId(comment.getId())
                .title(post.getTitle())
                .name(user.getName())
                .body(body)
                .build();
    }

    public void deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
        UserEntity user = comment.getCommentUser();
        Post post = comment.getCommentPost();
        user.getComments().remove(comment);
        post.getComments().remove(comment);

        commentRepository.delete(comment);
    }
}
