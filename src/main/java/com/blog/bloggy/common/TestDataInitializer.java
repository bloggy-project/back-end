package com.blog.bloggy.common;

import com.blog.bloggy.post.dto.PostDto;
import com.blog.bloggy.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataInitializer {
    private final PostService postService;
    @PostConstruct
    public void initTestData() {
        // 테스트 데이터 생성
        /*
        String userId = "c182fcb3-002e-4ff7-9cd4-cf53fb3cbe62";
        List<String> tagNames=new ArrayList<>();
        tagNames.add("소설");
        tagNames.add("영화");
        for(int i=1;i<=50;i++){
            String title="제목"+i;
            String content="본문 내용"+i;
            PostDto postDto= PostDto.builder()
                    .title(title)
                    .content(content)
                    .userId(userId)
                    .tagNames(tagNames)
                    .build();
            postService.createPost(postDto);
        }
        */

    }

    @PreDestroy
    public void cleanUpTestData() {
        // 테스트 데이터 삭제

    }
}
