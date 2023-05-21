package com.blog.summer.service;

import com.blog.summer.domain.Favorite;
import com.blog.summer.domain.Post;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.favorite.FavoriteDto;
import com.blog.summer.dto.favorite.ResponseFavoriteClick;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.favorite.FavoriteRepository;
import com.blog.summer.repository.post.PostRepository;
import com.blog.summer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public ResponseFavoriteClick createFavorite(FavoriteDto favoriteDto) {
        Long postId = favoriteDto.getPostId();
        String userId = favoriteDto.getUserId();
        Post post = postRepository.findByIdWithUserComment(postId).orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다"));
        UserEntity user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));;
        Favorite favorite = new Favorite();
        favorite.setAddFavorite(post);
        favoriteRepository.save(favorite);
        return ResponseFavoriteClick.builder()
                .postId(postId)
                .userId(userId)
                .build();
    }
    public void addFavoriteToPost(FavoriteDto favoriteDto) {
        Long postId = favoriteDto.getPostId();
        String userId = favoriteDto.getUserId();
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다."));
        UserEntity user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을수없습니다"));;

        favoriteRepository.findByFavoritePostAndFavoriteUser(post, user).ifPresentOrElse(
                (favorite)-> new NotFoundException("User already liked this post"),
                ()->{
                    Favorite favorite=new Favorite();
                    favorite.setRegisterFavorite(post,user);
                    favoriteRepository.save(favorite);
                }
        );
    }
    public void removeFavoriteToPost(FavoriteDto favoriteDto) {
        Long postId = favoriteDto.getPostId();
        String userId = favoriteDto.getUserId();
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다."));
        UserEntity user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을수없습니다"));
        Favorite favorite = favoriteRepository.findByFavoritePostAndFavoriteUser(post, user)
                .orElseThrow(() -> new NotFoundException("User has not liked this pos"));
        post.removeFavorite(favorite);
        favoriteRepository.delete(favorite);
    }
}
