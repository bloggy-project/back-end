package com.blog.bloggy.comment.service;


import com.blog.bloggy.comment.dto.*;
import com.blog.bloggy.comment.model.Comment;
import com.blog.bloggy.common.exception.*;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.repository.UserRepository;
import com.blog.bloggy.comment.repository.CommentRepository;
import com.blog.bloggy.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final static int MAX_DEPTH= 4;

    public ResponseCommentRegister createComment(CommentDto commentDto) {
        Long postId = commentDto.getPostId();
        String body = commentDto.getBody();
        Post post = postRepository.findByIdWithUser(postId).orElseThrow(PostNotFoundException::new);
        Comment comment = new Comment();
        UserEntity user = userRepository.findByUserId(commentDto.getUserId()).orElseThrow(UserNotFoundException::new);

        comment.setRegisterComment(post,user,body,user.getName(), CommentStatus.REGISTERED);
        commentRepository.save(comment);
        return ResponseCommentRegister.builder()
                .commentId(comment.getId())
                .title(post.getTitle())
                .name(user.getName())
                .body(body)
                .build();
    }
    public ResponseRecommentRegister createRecomment(RecommentDto commentDto) {
        Long postId = commentDto.getPostId();
        String body = commentDto.getBody();
        Post post = postRepository.findByIdWithUser(postId).orElseThrow(PostNotFoundException::new);
        Comment parent = commentRepository.findById(commentDto.getCommentId()).
                orElseThrow(CommentNotFoundException::new);
        if(parent.getDepth()>=MAX_DEPTH){
            throw new DepthOverReplyException();
        }
        Comment comment = new Comment();
        UserEntity user = userRepository.findByUserId(commentDto.getUserId()).orElseThrow(UserNotFoundException::new);
        comment.setRegisterComment(post,user,body,user.getName(), CommentStatus.REGISTERED);
        comment.setParentId(parent.getParentId(),parent.getDepth());
        commentRepository.save(comment);
        return ResponseRecommentRegister.builder()
                .commentId(comment.getId())
                .parentId(parent.getId())
                .depth(comment.getDepth())
                .title(post.getTitle())
                .name(user.getName())
                .body(body)
                .build();
    }

    public void deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException());
        UserEntity user = comment.getCommentUser();
        Post post = comment.getCommentPost();
        user.getComments().remove(comment);
        post.getComments().remove(comment);

        commentRepository.delete(comment);
    }
}
