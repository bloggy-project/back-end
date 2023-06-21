package com.blog.bloggy.common.repository;

import com.blog.bloggy.common.dto.TrendSearchCondition;
import com.blog.bloggy.favorite.model.Favorite;
import com.blog.bloggy.favorite.repository.FavoriteRepository;
import com.blog.bloggy.post.dto.ResponsePostList;
import com.blog.bloggy.post.dto.ResponseUserPagePost;
import com.blog.bloggy.post.dto.ResponseUserPagePostWithPostTags;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.post.repository.PostRepository;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@DataJpaTest //@Transactional 포함
@Import(PagingQueryRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PagingQueryRepositoryTest {
    @Autowired
    private PagingQueryRepository pagingQueryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    String username1 = "abcd123";
    String username2 = "efgh456";

    @BeforeEach
    void init() {
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
    }


    @Test
    @Transactional
    void findPostsForMain() {

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ResponsePostList> postsForMain = pagingQueryRepository.findPostsForMain(null, pageable);
        for (ResponsePostList responsePostList : postsForMain) {
            System.out.println("responsePostList = " + responsePostList);
        }
        List<ResponsePostList> collect = postsForMain.get().collect(toList());
        if(collect.size()!=0){
            Long lastId = collect.get(collect.size() - 1).getPostId();
            Slice<ResponsePostList> postsForMain2 = pagingQueryRepository.findPostsForMain(lastId, pageable);
            for (ResponsePostList responsePostList : postsForMain2) {
                System.out.println("nextResponsePostList = " + responsePostList);
            }
        }
    }

    @Test
    void findPostsFromMainTrend() {
        int page = 0;
        int size = 15;
        Pageable pageable = PageRequest.of(page, size);
        TrendSearchCondition condition = new TrendSearchCondition();
        Slice<ResponsePostList> postsForMain = pagingQueryRepository.findPostsForMainTrend(condition, pageable);
        for (ResponsePostList responsePostList : postsForMain) {
            System.out.println("responsePostList = " + responsePostList);
        }
        List<ResponsePostList> collect = postsForMain.get().collect(toList());
        if(collect.size()!=0){
            Long cnt = collect.get(collect.size() - 1).getFavoriteCount();
            condition.setFavorites(cnt);
            Slice<ResponsePostList> postsForMain2 = pagingQueryRepository.findPostsForMainTrend(condition, pageable);
            for (ResponsePostList responsePostList : postsForMain2) {
                System.out.println("nextResponsePostList = " + responsePostList);
            }
        }

    }

    @Test
    void findUserPostsOrderByCreatedAtV2() {
        Pageable pageable = PageRequest.of(2, 5);
        Page<Post> posts = pagingQueryRepository.findUserPostsOrderByCreatedAtV2(username1, pageable);
        for (Post post : posts) {
            System.out.println("post.getPostUser().getName() = " + post.getPostUser().getName());
        }
        Page<ResponseUserPagePost> toMap = posts.map(post -> ResponseUserPagePost.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                //.tagNames(post.getPostTags().stream().map(tag -> tag.getTagName()).collect(toList()))
                .build());
        for (ResponseUserPagePost responseUserPagePost : toMap) {
            System.out.println("responseUserPagePost = " + responseUserPagePost);
        }
    }

    @Test
    void findUserPostsOrderByCreatedAtV3() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ResponseUserPagePost> gon = pagingQueryRepository.findUserPostsOrderByCreatedAtV3(username1, pageable);
        for (ResponseUserPagePost responseUserPagePost : gon) {
            System.out.println("responseUserPagePost = " + responseUserPagePost);
        }
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