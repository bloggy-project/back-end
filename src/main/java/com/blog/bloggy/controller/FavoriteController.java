package com.blog.bloggy.controller;


import com.blog.bloggy.dto.favorite.RequestFavorite;
import com.blog.bloggy.dto.favorite.FavoriteDto;
import com.blog.bloggy.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FavoriteController {


    private final FavoriteService favoriteService;

    @PostMapping("/favorite/like")
    public void addFavorite(@RequestBody RequestFavorite requestFavorite){
        FavoriteDto favoriteDto= FavoriteDto.builder()
                .postId(requestFavorite.getPostId())
                .userId(requestFavorite.getUserId())
                .build();
        favoriteService.addFavoriteToPost(favoriteDto);
    }
    @PostMapping("/favorite/unlike")
    public void removeFavorite(@RequestBody RequestFavorite requestFavorite){
        FavoriteDto favoriteDto= FavoriteDto.builder()
                .postId(requestFavorite.getPostId())
                .userId(requestFavorite.getUserId())
                .build();
        favoriteService.removeFavoriteToPost(favoriteDto);
    }
}
