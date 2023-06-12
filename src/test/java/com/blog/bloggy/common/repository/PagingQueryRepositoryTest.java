package com.blog.bloggy.common.repository;

import com.blog.bloggy.post.dto.ResponsePostList;
import com.blog.bloggy.post.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PagingQueryRepositoryTest {

    @Autowired
    private PagingQueryRepository repository;

    @Test
    void findPostsForMainV1() {
        int page=0; int size=10;
        Pageable pageable = PageRequest.of(page, size);
        Slice<Post> posts = repository.findPostsForMainV1(null, pageable);
        List<Post> list = posts.getContent();
        for (Post post : list) {
            System.out.println("post = " + post);
        }
    }

    @Test
    void findPostsForMainV2() {
        int page=0; int size=10;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ResponsePostList> postsForMainV2 = repository.findPostsForMainV2(200L, pageable);
        for (ResponsePostList responsePostList : postsForMainV2) {
            System.out.println("responsePostList = " + responsePostList);
        }
    }
}