package com.blog.summer.service;


import com.blog.summer.domain.Comment;
import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.comment.CommentDto;
import com.blog.summer.dto.comment.CommentStatus;
import com.blog.summer.dto.comment.ResponseCommentRegister;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.UserRepository;
import com.blog.summer.repository.comment.CommentRepository;
import com.blog.summer.repository.post.PostRepository;
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
