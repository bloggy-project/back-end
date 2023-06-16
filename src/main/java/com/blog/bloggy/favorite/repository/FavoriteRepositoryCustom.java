package com.blog.bloggy.favorite.repository;


import java.util.List;

public interface FavoriteRepositoryCustom {
    public void deleteFavoritesByPostId(Long postId);

    public List<String> getPostsFavoritesUsernames(Long postId);
}
