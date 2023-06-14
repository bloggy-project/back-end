package com.blog.bloggy.common.repository;

import com.blog.bloggy.comment.service.CommentService;
import com.blog.bloggy.favorite.dto.FavoriteDto;
import com.blog.bloggy.favorite.service.FavoriteService;
import com.blog.bloggy.post.dto.PostDto;
import com.blog.bloggy.post.dto.ResponsePostList;
import com.blog.bloggy.post.dto.ResponsePostRegister;
import com.blog.bloggy.post.dto.ResponseUserPagePost;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.post.service.PostService;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
@DataJpaTest //@Transactional 포함
class PagingQueryRepositoryTest {

    @Autowired
    private PagingQueryRepository pagingQueryRepository;
    @BeforeEach
    public void setup() throws InterruptedException {
    }

    @Test
    void findPostsForMain() {
        int page=0; int size=10;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ResponsePostList> postsForMain = pagingQueryRepository.findPostsForMain(null, pageable);
        for (ResponsePostList responsePostList : postsForMain) {
            System.out.println("responsePostList = " + responsePostList);
        }
    }

    @Test
    void findPostsFromMainTrend() throws InterruptedException {
        int page=0; int size=10;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ResponsePostList> postsForMain = pagingQueryRepository.findPostsForMainTrend(null, pageable);
        for (ResponsePostList responsePostList : postsForMain) {
            System.out.println("responsePostList = " + responsePostList);
        }
    }

    @Test
    void findUserPostsOrderByCreatedAtV2() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Post> posts = pagingQueryRepository.findUserPostsOrderByCreatedAtV2("gon1", pageable);
        Page<ResponseUserPagePost> toMap = posts.map(post -> ResponseUserPagePost.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .tagNames(post.getPostTags().stream().map(tag-> tag.getTagName()).collect(toList()))
                .build());
        for (ResponseUserPagePost responseUserPagePost : toMap) {
            System.out.println("responseUserPagePost = " + responseUserPagePost);
        }
    }
    @Test
    void findUserPostsOrderByCreatedAtV3() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ResponseUserPagePost> gon = pagingQueryRepository.findUserPostsOrderByCreatedAtV3("gon1", pageable);
        for (ResponseUserPagePost responseUserPagePost : gon) {
            System.out.println("responseUserPagePost = " + responseUserPagePost);
        }
    }
}