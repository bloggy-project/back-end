package com.blog.bloggy.post.dto;

import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.comment.dto.CommentDto;
import com.blog.bloggy.favorite.dto.FavoriteDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.*;

@Data
public class PostAllDto {
    private Long postId;

    private String title;

    private String content;

    private String categoryName;

    private List<CommentDto> comments=new ArrayList<>();

    private List<FavoriteDto> favorites=new ArrayList<>();
    public PostAllDto(Post p) {
        postId=p.getId();
        title=p.getTitle();
        content=p.getContent();
        comments=p.getComments().stream()
                .map(comment -> CommentDto.builder()
                        .postId(comment.getId())
                        .body(comment.getBody())
                        .build()).collect(toList());
        favorites=p.getFavorites().stream()
                .map(favorite -> FavoriteDto.builder()
                        .build()).collect(toList());
    }
}
