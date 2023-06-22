package com.blog.bloggy.common.repository;

import com.blog.bloggy.common.dto.TrendSearchCondition;
import com.blog.bloggy.favorite.model.Favorite;
import com.blog.bloggy.favorite.repository.FavoriteRepository;
import com.blog.bloggy.post.dto.ResponsePostList;
import com.blog.bloggy.post.dto.ResponseUserPagePost;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.post.repository.PostRepository;
import com.blog.bloggy.postTag.model.PostTag;
import com.blog.bloggy.postTag.repository.PostTagRepository;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@SpringBootTest
@Slf4j
//@DataJpaTest //@Transactional 포함
//@Import(PagingQueryRepository.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PagingQueryRepositoryTest {
    @Autowired
    private PagingQueryRepository pagingQueryRepository;
    @Autowired
    private PostTagRepository postTagRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    private long lastId=25000;
    private long startTime;
    String username1 = "abcd123";
    String username2 = "efgh456";
    String username3 = "efgh789";
    String username4 = "test123";
    String username5 = "bslv987";
    String username6 = "knef337";

    //@BeforeEach
    //@Rollback(false)
    void init() {

        List<String> tagNames=new ArrayList<>();
        tagNames.add("소설");
        tagNames.add("영화");

        UserEntity user1 = getUserEntity(username1);
        UserEntity user2 = getUserEntity(username2);
        UserEntity user3 = getUserEntity(username3);
        UserEntity user4 = getUserEntity(username4);
        UserEntity user5 = getUserEntity(username5);
        UserEntity user6 = getUserEntity(username6);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);
        for (int i = 0; i < 100000; i++) {
            if(i%2==0) {
                Post post = makeTest(user1, i);
                for (String tagName : tagNames) {
                    PostTag.updatePostTag(post,tagName);
                }
                PostTag.updatePostTag(post,"tag"+i);
                getFavorites(user1, user2, user3, user4, user5, user6, i, post);
            }
            else if(i%3==0) {
                Post post = makeTest(user2, i);
                for (String tagName : tagNames) {
                    PostTag.updatePostTag(post,tagName);
                }
                PostTag.updatePostTag(post,"tag"+i);
                getFavorites(user1, user2, user3, user4, user5, user6, i, post);

            }
            else if(i%5==0) {
                Post post = makeTest(user3, i);
                for (String tagName : tagNames) {
                    PostTag.updatePostTag(post,tagName);
                }
                PostTag.updatePostTag(post,"tag"+i);
                getFavorites(user1, user2, user3, user4, user5, user6, i, post);
            }
            else if(i%7==0){
                Post post = makeTest(user4, i);
                for (String tagName : tagNames) {
                    PostTag.updatePostTag(post,tagName);
                }
                PostTag.updatePostTag(post,"tag"+i);
                getFavorites(user1, user2, user3, user4, user5, user6, i, post);
            }
            else if(i%11==0){
                Post post = makeTest(user5, i);
                for (String tagName : tagNames) {
                    PostTag.updatePostTag(post,tagName);
                }
                PostTag.updatePostTag(post,"tag"+i);
                getFavorites(user1, user2, user3, user4, user5, user6, i, post);
            }
            else if(i%13==0){
                Post post = makeTest(user6, i);
                for (String tagName : tagNames) {
                    PostTag.updatePostTag(post,tagName);
                }
                PostTag.updatePostTag(post,"tag"+i);
                getFavorites(user1, user2, user3, user4, user5, user6, i, post);
            }
        }
    }



    private Post makeTest(UserEntity user1, int i) {
        Post post = Post.builder()
                .title("test" + i)
                .user(user1)
                .build();
        postRepository.save(post);
        post.setCreatedAt(post.getCreatedAt().minusDays(7).plusSeconds(i));
        return post;
    }

    private void getFavorites(UserEntity user1, UserEntity user2, UserEntity user3, UserEntity user4, UserEntity user5, UserEntity user6, int i, Post post) {
        getFavorite(user1, i, post);
        getFavorite(user2, i, post);
        getFavorite(user3, i, post);
        getFavorite(user4, i, post);
        getFavorite(user5, i, post);
        getFavorite(user6, i, post);
    }

    private void getFavorite(UserEntity user, int i, Post post) {
        for(int j = 0; j<(i %5); j++){
            Favorite favorite=Favorite.builder()
                    .favoritePost(post)
                    .favoriteUser(user)
                    .build();
            favoriteRepository.save(favorite);
            post.addFavorite(favorite);
        }
    }

    //@Test
    @DisplayName("Post객체를 User와 join하고 commment와 favorite count는 서브쿼리처리")
    void findPostsForMainUsingJoinUser() {
        int page = 5000;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ResponsePostList> postsForMain = pagingQueryRepository.findPostsForMainUsingJoinUser(lastId, pageable);

    }

    //@Test
    @DisplayName("Post객체를 index range 조회 후 comment, favorite user지연로딩 초기화 시간 측정")
    void findPostsForMainNotEagerAll() {
        int page = 5000;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ResponsePostList> postsForMain = pagingQueryRepository.findPostsForMainNotEagerAll(lastId, pageable);

    }
    //@Test
    @DisplayName("Post객체를 index range 조회 후 서브쿼리+user는 지연로딩 초기화 시간 측정")
    void findPostsForMainUsingUserNotJoin() {

        int page = 5000;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ResponsePostList> postsForMain = pagingQueryRepository.findPostsForMainUsingUserNotJoin(lastId, pageable);
    }


    //@Test
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
    @DisplayName("username의 userId를 인덱스로 빠르게 찾고 default batch size로 postTag 가져오는 쿼리")
    void findUserPostsOrderByCreatedDTO() {
        Pageable pageable = PageRequest.of(400, 5);
        Page<ResponseUserPagePost> posts =
                pagingQueryRepository.findUserPostsOrderByCreatedDTO(username1, pageable);

        for (ResponseUserPagePost post : posts) {
            log.info("post =  {}",post );
        }
    }
    /*
    @Test
    @DisplayName("join을 사용하여 ids를 가져오는 index 미사용 쿼리")
    void findUserPostsOrderByCreatedJoin() {
        Pageable pageable = PageRequest.of(400, 5);
        Page<ResponseUserPagePost> posts =
                pagingQueryRepository.findUserPostsOrderByCreatedJoin(username1, pageable);

        for (ResponseUserPagePost post : posts) {
            log.info("post =  {}",post );
        }
    }

    @Test
    @DisplayName("username의 userId를 인덱스로 빠르게 찾고 fetchJoin으로 postTag를 미리 가져오는 쿼리")
    void findUserPostsOrderByCreatedFetchJoin() {
        Pageable pageable = PageRequest.of(400, 5);
        Page<ResponseUserPagePost> posts =
                pagingQueryRepository.findUserPostsOrderByCreatedFetchJoin(username1, pageable);
        for (ResponseUserPagePost post : posts) {
            log.info("post =  {}",post );
        }
    }
    */

    private static UserEntity getUserEntity(String username) {
        UserEntity user = UserEntity.builder()
                .email(username + "@naver.com")
                .name(username)
                .userId(username)
                .build();
        return user;
    }
}