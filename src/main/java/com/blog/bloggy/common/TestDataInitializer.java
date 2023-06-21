package com.blog.bloggy.common;

import com.blog.bloggy.common.repository.PagingQueryRepository;
import com.blog.bloggy.favorite.model.Favorite;
import com.blog.bloggy.favorite.repository.FavoriteRepository;
import com.blog.bloggy.post.dto.PostDto;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.post.repository.PostRepository;
import com.blog.bloggy.post.service.PostService;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataInitializer {
    private final PostService postService;


    private final PagingQueryRepository pagingQueryRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FavoriteRepository favoriteRepository;
    String username1 = "abcd123";
    String username2 = "efgh456";

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
        d
        */
        /*
        UserEntity user1 = getUserEntity(username1);
        UserEntity user2 = getUserEntity(username2);
        userRepository.save(user1);
        userRepository.save(user2);
        for (int i = 0; i < 30; i++) {
            if(i%2==0) {
                Post post = Post.builder()
                        .title("test" + i)
                        .user(user1)
                        .build();
                postRepository.save(post);
                post.setCreatedAt(post.getCreatedAt().minusDays(7).plusHours(i));
                for(int j=0;j<(i%4); j++){
                    Favorite favorite=Favorite.builder()
                            .favoritePost(post)
                            .favoriteUser(user1)
                            .build();
                    favoriteRepository.save(favorite);
                    post.addFavorite(favorite);
                }
            }
            if(i%2==1) {
                Post post = Post.builder()
                        .title("test" + i)
                        .user(user2)
                        .build();
                postRepository.save(post);
                post.setCreatedAt(post.getCreatedAt().minusDays(7).plusHours(i));
                for(int j=0;j<(i%4); j++){
                    Favorite favorite=Favorite.builder()
                            .favoritePost(post)
                            .favoriteUser(user1)
                            .build();
                    favoriteRepository.save(favorite);
                    post.addFavorite(favorite);
                }
            }
        }
        */

    }

    @PreDestroy
    public void cleanUpTestData() {
        // 테스트 데이터 삭제

    }
    private static UserEntity getUserEntity(String username) {
        UserEntity user = UserEntity.builder()
                .email(username + "@naver.com")
                .name(username)
                .userId(username)
                .build();
        return user;
    }
}
