package com.blog.bloggy.common.repository;

import com.blog.bloggy.comment.repository.CommentRepository;
import com.blog.bloggy.comment.service.CommentService;
import com.blog.bloggy.favorite.dto.FavoriteDto;
import com.blog.bloggy.favorite.repository.FavoriteRepository;
import com.blog.bloggy.favorite.service.FavoriteService;
import com.blog.bloggy.post.dto.PostDto;
import com.blog.bloggy.post.dto.ResponsePostList;
import com.blog.bloggy.post.dto.ResponsePostRegister;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.post.repository.PostRepository;
import com.blog.bloggy.post.service.PostService;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PagingQueryRepositoryTest {

    @Autowired
    private PagingQueryRepository pagingQueryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FavoriteService favoriteService;

    @BeforeEach
    public void setup() throws InterruptedException {
    }

    private void init() throws InterruptedException {
        UserEntity userA=new UserEntity("test1@naver.com","gon1","abcde1");
        UserEntity userB=new UserEntity("test2@naver.com","go2","abcde2");
        userRepository.save(userA);
        userRepository.save(userB);
        List<String> tagNames=new ArrayList<>();
        tagNames.add("소설");
        tagNames.add("영화");
        createPosts(userA, tagNames);
        tagNames.add("게임");
        createPosts(userB,tagNames);
        //외래키 제거 후 다시 진행 예정
    }

    private void createPosts(UserEntity user, List<String> tagNames)  {
        for(int i=0;i<=10;i++){
            String title="제목"+i+" "+user.getEmail();
            String content="본문 내용"+i+" "+ user.getName();
            PostDto postDto= PostDto.builder()
                    .title(title)
                    .content(content)
                    .userId(user.getUserId())
                    .tagNames(tagNames)
                    .build();
            ResponsePostRegister post = postService.createPost(postDto);
            //Thread.sleep(200);
            if(i%2==0) {
                FavoriteDto favoriteDto = FavoriteDto.builder()
                        .postId(post.getPostId())
                        .userId(user.getUserId())
                        .build();
                favoriteService.addFavoriteToPost(favoriteDto);
            }

        }

    }

    @Test
    void findPostsForMain() {
        int page=0; int size=10;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ResponsePostList> postsForMain = pagingQueryRepository.findPostsForMain(200L, pageable);
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
}