package com.blog.summer.dto.post;

import com.blog.summer.domain.Comment;
import com.blog.summer.domain.Favorite;
import com.blog.summer.domain.Post;
import com.blog.summer.dto.comment.CommentDto;
import com.blog.summer.dto.favorite.FavoriteDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
