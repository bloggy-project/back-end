package com.blog.bloggy.favorite.service;

import com.blog.bloggy.common.exception.*;
import com.blog.bloggy.favorite.model.Favorite;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.favorite.dto.FavoriteDto;
import com.blog.bloggy.favorite.dto.ResponseFavoriteClick;
import com.blog.bloggy.favorite.repository.FavoriteRepository;
import com.blog.bloggy.post.repository.PostRepository;
import com.blog.bloggy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseFavoriteClick addFavoriteToPost(FavoriteDto favoriteDto) {
        Long postId = favoriteDto.getPostId();
        String userId = favoriteDto.getUserId();
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException());
        UserEntity user = userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException());

        favoriteRepository.findByFavoritePostAndFavoriteUser(post, user).ifPresentOrElse(
                (favorite)-> {throw new DataAlreadyExistException();},
                ()->{
                    Favorite favorite=new Favorite();
                    favoriteRepository.save(favorite);
                    favorite.setRegisterFavorite(post,user);
                }
        );
        ResponseFavoriteClick response = ResponseFavoriteClick.builder()
                .postId(post.getId())
                .username(user.getName())
                .build();
        return response;
    }
    @Transactional
    public void removeFavoriteToPost(FavoriteDto favoriteDto) {
        Long postId = favoriteDto.getPostId();
        String userId = favoriteDto.getUserId();
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException());
        UserEntity user = userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException());
        Favorite favorite = favoriteRepository.findByFavoritePostAndFavoriteUser(post, user)
                .orElseThrow(() -> new DataAlreadyRemoveException());
        post.removeFavorite(favorite);
        favoriteRepository.delete(favorite);
    }
}
