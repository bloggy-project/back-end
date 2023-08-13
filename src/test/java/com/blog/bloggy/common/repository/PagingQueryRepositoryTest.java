package com.blog.bloggy.common.repository;

import com.blog.bloggy.common.dto.TrendSearchCondition;
import com.blog.bloggy.favorite.model.Favorite;
import com.blog.bloggy.favorite.repository.FavoriteRepository;
import com.blog.bloggy.post.dto.ResponsePostList;
import com.blog.bloggy.post.dto.ResponseUserPagePost;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.post.repository.PostRepository;
import com.blog.bloggy.postTag.dto.PostTagStatus;
import com.blog.bloggy.postTag.model.PostTag;
import com.blog.bloggy.postTag.repository.PostTagRepository;
import com.blog.bloggy.tag.model.Tag;
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
import java.util.Random;

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
                    updatePostTag(post,tagName);
                }
                updatePostTag(post,"tag"+i);
                ////getFavorites(user1, user2, i, post);
            }
            else if(i%3==0) {
                Post post = makeTest(user2, i);
                for (String tagName : tagNames) {
                    updatePostTag(post,tagName);
                }
                updatePostTag(post,"tag"+i);
                //getFavorites(user1, user2, i, post);

            }
            else if(i%5==0) {
                Post post = makeTest(user3, i);
                for (String tagName : tagNames) {
                    updatePostTag(post,tagName);
                }
                updatePostTag(post,"tag"+i);
                //getFavorites(user1, user2, i, post);
            }
            else if(i%7==0){
                Post post = makeTest(user4, i);
                for (String tagName : tagNames) {
                    updatePostTag(post,tagName);
                }
                updatePostTag(post,"tag"+i);
                //getFavorites(user1, user2, i, post);
            }
            else if(i%11==0){
                Post post = makeTest(user5, i);
                for (String tagName : tagNames) {
                    updatePostTag(post,tagName);
                }
                updatePostTag(post,"tag"+i);
                //getFavorites(user1, user2, i, post);
            }
            else if(i%13==0){
                Post post = makeTest(user6, i);
                for (String tagName : tagNames) {
                    updatePostTag(post,tagName);
                }
                updatePostTag(post,"tag"+i);
                //getFavorites(user1, user2, i, post);
            }
        }
    }

    private Post makeTest(UserEntity user1, int i) {
        Random random = new Random();
        Post post = Post.builder()
                .title("test" + i)
                .user(user1)
                .favoriteCount(random.nextInt(9))
                .build();
        postRepository.save(post);
        post.setCreatedAt(post.getCreatedAt().minusDays(7).plusSeconds(i+random.nextInt(10000)));
        return post;
    }

    private void getFavorites(UserEntity user1, UserEntity user2, int i, Post post) {
        getFavorite(user1, i, post);
        getFavorite(user2, i, post);
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


    @Test
    @DisplayName("User와 join하고 commment와 favorite count는 서브쿼리처리")
    void findPostsForMain() {
        int page = 0;
        int size = 15;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ResponsePostList> posts = pagingQueryRepository.findPostsForMain(lastId, pageable);
    }

    @Test
    @DisplayName("User의 게시물 최신순으로 정렬하는 쿼리")
    void findUserPostsOrderByCreated() {
        Pageable pageable = PageRequest.of(500, 5);
        Page<ResponseUserPagePost> posts =
                pagingQueryRepository.findUserPostsOrderByCreated(1L, pageable);
    }


    @Test
    void findPostsFromMainTrend() {
        int page = 0;
        int size = 15;
        Pageable pageable = PageRequest.of(page, size);
        TrendSearchCondition condition = TrendSearchCondition.builder()
                .favorCount(4L)
                .lastId(10000L)
                .build();
        Slice<ResponsePostList> postsForMain = pagingQueryRepository.findPostsForMainTrendV1(condition, pageable);
        for (ResponsePostList responsePostList : postsForMain) {
            System.out.println("responsePostList = " + responsePostList);
        }
        /*
        List<ResponsePostList> collect = postsForMain.get().collect(toList());
        if(collect.size()!=0){
            Long cnt = collect.get(collect.size() - 1).getFavoriteCount();
            Long postId = collect.get(collect.size() - 1).getPostId();
            condition.setFavorCount(cnt);
            condition.setLastId(postId);
            Slice<ResponsePostList> postsForMain2 = pagingQueryRepository.findPostsForMainTrend(condition, pageable);
            for (ResponsePostList responsePostList : postsForMain2) {
                System.out.println("nextResponsePostList = " + responsePostList);
            }
        }
         */
    }
    @Test
    void findPostsFromMainTrendV2() {
        int page = 0;
        int size = 15;
        Pageable pageable = PageRequest.of(page, size);
        TrendSearchCondition condition = TrendSearchCondition.builder()
                .favorCount(4L)
                .lastId(10000L)
                .build();
        Slice<ResponsePostList> postsForMain = pagingQueryRepository.findPostsForMainTrendV2(condition, pageable);
        for (ResponsePostList responsePostList : postsForMain) {
            System.out.println("responsePostList = " + responsePostList);
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

    // 테스트 끝난 미사용 코드
    /*
    @Test
    @DisplayName("username의 userId를 인덱스로 빠르게 찾고 default batch size로 postTag 가져오는 쿼리")
    void findUserPostsOrderByCreated() {
        Pageable pageable = PageRequest.of(500, 5);
        Page<ResponseUserPagePost> posts =
                pagingQueryRepository.findUserPostsOrderByCreatedOld(username1, pageable);
        for (ResponseUserPagePost post : posts) {
            log.info("post =  {}",post );
        }
    }
     */

    /*
    @Test
    @DisplayName("comment, favorite, user 지연로딩 초기화")
    void findPostsForMainNotEagerAll() {
        int page = 0;
        int size = 15;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ResponsePostList> posts = pagingQueryRepository.findPostsForMainNotEagerAll(lastId, pageable);
    }
     */

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

    */

    /*
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

    private PostTag updatePostTag(Post post, String tagName) {
        PostTag postTag=PostTag.builder()
                .tagName(tagName)
                .tagPost(post)
                .status(PostTagStatus.UPDATED)
                .build();

        post.addPostTag(postTag);
        return postTag;
    }
    private PostTag createPostTag(Post post, Tag tag, String tagName) {
        PostTag postTag = PostTag.builder()
                .tagPost(post)
                .tag(tag)
                .tagName(tagName)
                .status(PostTagStatus.REGISTERED)
                .build();
        post.addPostTag(postTag);
        //tag.addPostTag(postTag);
        return postTag;
    }
}