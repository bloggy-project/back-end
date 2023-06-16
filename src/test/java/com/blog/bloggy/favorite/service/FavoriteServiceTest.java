package com.blog.bloggy.favorite.service;

import com.blog.bloggy.common.exception.DataAlreadyExistException;
import com.blog.bloggy.common.exception.DataAlreadyRemoveException;
import com.blog.bloggy.favorite.dto.FavoriteDto;
import com.blog.bloggy.favorite.dto.ResponseFavoriteClick;
import com.blog.bloggy.favorite.model.Favorite;
import com.blog.bloggy.favorite.repository.FavoriteRepository;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.post.repository.PostRepository;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    @DisplayName("게시물의 Favorite 클릭 성공")
    void addFavoriteToPost_Success() {
        Long postId= 1L;
        FavoriteDto favoriteDto = new FavoriteDto(postId, "testUserId");
        Post post = new Post(postId);
        UserEntity user = new UserEntity(null,"testUser","testUserId");

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));
        given(userRepository.findByUserId(any(String.class))).willReturn(Optional.of(user));
        given(favoriteRepository.findByFavoritePostAndFavoriteUser(any(Post.class), any(UserEntity.class)))
                .willReturn(Optional.empty());

        ResponseFavoriteClick responseFavorite = favoriteService.addFavoriteToPost(favoriteDto);

        assertEquals(post.getId(),responseFavorite.getPostId());
        assertEquals(user.getName(), responseFavorite.getUsername());
        assertEquals(1,post.getFavorites().size());
        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }
    @Test
    @DisplayName("게시물의 Favorite 존재 시")
    void addFavoriteToPost_AlreadyExist() {
        Long postId= 1L;
        FavoriteDto favoriteDto = new FavoriteDto(postId, "testUserId");
        Post post = new Post(postId);
        UserEntity user = new UserEntity(null,"testUser","testUserId");
        Favorite favorite=new Favorite();
        favorite.setRegisterFavorite(post,user);

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));
        given(userRepository.findByUserId(any(String.class))).willReturn(Optional.of(user));
        given(favoriteRepository.findByFavoritePostAndFavoriteUser(post, user))
                .willReturn(Optional.of(favorite));
        assertThrows(DataAlreadyExistException.class, ()-> favoriteService.addFavoriteToPost(favoriteDto));
        assertEquals(1,post.getFavorites().size());
        verify(favoriteRepository, times(0)).save(any(Favorite.class));
    }

    @Test
    @DisplayName("로그인 회원 게시물 Favorite 취소")
    void removeFavoriteToPost() {
        Long postId= 1L;
        FavoriteDto favoriteDto = new FavoriteDto(postId, "testUserId");
        Post post = new Post(postId);
        UserEntity user = new UserEntity(null,"testUser","testUserId");
        Favorite favorite=new Favorite();
        favorite.setRegisterFavorite(post,user);
        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));
        given(userRepository.findByUserId(any(String.class))).willReturn(Optional.of(user));
        given(favoriteRepository.findByFavoritePostAndFavoriteUser(post, user))
                .willReturn(Optional.of(favorite));

        favoriteService.removeFavoriteToPost(favoriteDto);
        assertEquals(0,post.getFavorites().size());
        verify(favoriteRepository, times(1)).delete(favorite);
    }
    @Test
    @DisplayName("로그인 회원 게시물 Favorite 이미 취소")
    void removeFavoriteToPost_alreadyCancel() {
        Long postId= 1L;
        FavoriteDto favoriteDto = new FavoriteDto(postId, "testUserId");
        Post post = new Post(postId);
        UserEntity user = new UserEntity(null,"testUser","testUserId");
        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));
        given(userRepository.findByUserId(any(String.class))).willReturn(Optional.of(user));
        given(favoriteRepository.findByFavoritePostAndFavoriteUser(post, user))
                .willReturn(Optional.empty());

        assertThrows(DataAlreadyRemoveException.class,
                ()-> favoriteService.removeFavoriteToPost(favoriteDto));

        assertEquals(0,post.getFavorites().size());
    }
}