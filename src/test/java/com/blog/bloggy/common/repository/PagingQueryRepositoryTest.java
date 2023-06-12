package com.blog.bloggy.common.repository;

import com.blog.bloggy.post.dto.ResponsePostList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@SpringBootTest
class PagingQueryRepositoryTest {

    @Autowired
    private PagingQueryRepository repository;




    @Test
    void findPostsForMainV2() {
        int page=0; int size=10;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ResponsePostList> postsForMainV2 = repository.findPostsForMain(200L, pageable);
        for (ResponsePostList responsePostList : postsForMainV2) {
            System.out.println("responsePostList = " + responsePostList);
        }
    }
}