package com.blog.bloggy.post.service;

import com.blog.bloggy.post.dto.PostDto;
import com.blog.bloggy.post.dto.PostUpdateDto;
import com.blog.bloggy.post.dto.ResponsePostRegister;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.post.repository.PostRepository;
import com.blog.bloggy.postTag.dto.PostTagStatus;
import com.blog.bloggy.postTag.model.PostTag;
import com.blog.bloggy.postTag.repository.PostTagRepository;
import com.blog.bloggy.tag.repository.TagRepository;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostTagRepository postTagRepository;

    @Test
    void createPost() {
        // Given
        String userId = "testUser";
        String title = "Test Title";
        String content = "Test Content";
        List<String> tagNames = getTagNames2();

        UserEntity user = getUserEntity(userId);

        PostDto postDto = getPostDto(userId, title, content, tagNames);

        when(userRepository.findByUserId(postDto.getUserId()))
                .thenReturn(Optional.of(user));

        for(String tagName: tagNames){
            given(tagRepository.findByName(tagName)).willReturn(Optional.empty());
        }
        //when
        ResponsePostRegister response = postService.createPost(postDto);

        //then
        assertEquals(userId,response.getUserId());
        assertEquals(title, response.getTitle());
        assertEquals(content, response.getContent());
        assertEquals(tagNames, response.getTagNames());
    }

    private static PostDto getPostDto(String userId, String title, String content, List<String> tagNames) {
        PostDto postDto = PostDto.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .tagNames(tagNames)
                .build();
        return postDto;
    }

    private static UserEntity getUserEntity(String userId) {
        UserEntity user = UserEntity.builder()
                .email("test1234@naver.com")
                .name("gon")
                .userId(userId)
                .build();
        return user;
    }

    private static List<String> getTagNames2() {
        List<String> tagNames = new ArrayList<>();
        tagNames.add("tag1");
        tagNames.add("tag2");
        return tagNames;
    }
    private static List<String> getTagNames4() {
        List<String> tagNames = new ArrayList<>();
        tagNames.add("tag1");
        tagNames.add("tag2");
        tagNames.add("tag3");
        tagNames.add("tag4");
        return tagNames;
    }



    @Test
    @DisplayName("게시물 수정_기존 태그 삭제")
    void updatePost() {
        // Given
        Long postId = 1L;
        List<String> tagNames = getTagNames2();
        PostUpdateDto postUpdateDto =PostUpdateDto.builder()
                .postId(postId)
                .title("new title")
                .content("new content")
                .tagNames(tagNames)
                .build();

        List<PostTag> postTags=new ArrayList<>();
        PostTag postTag1= PostTag.builder()
                .id(1L)
                .tagName("tag1")
                .status(PostTagStatus.UPDATED)
                .build();
        PostTag postTag2= PostTag.builder()
                .id(2L)
                .tagName("tag2")
                .status(PostTagStatus.UPDATED)
                .build();
        PostTag postTag3= PostTag.builder()
                .id(3L)
                .tagName("tag3")
                .status(PostTagStatus.UPDATED)
                .build();
        postTags.add(postTag1);
        postTags.add(postTag2);
        postTags.add(postTag3);
        Post post= Post.builder()
                .id(postId)
                .title("title")
                .content("content")
                .postTags(postTags)
                .build();

        given(postRepository.findByIdWithPostTag(postId)).willReturn(Optional.of(post));
        //When
        ResponsePostRegister result = postService.updatePost(postUpdateDto);
        List<String> newTagNames = post.getPostTags().stream().map(postTag -> postTag.getTagName())
                .collect(Collectors.toList());
        assertEquals(postId, result.getPostId());
        assertEquals(postUpdateDto.getTitle(), result.getTitle());
        assertEquals(postUpdateDto.getContent(), result.getContent());
        assertEquals(postTag1.getStatus(),PostTagStatus.UPDATED);
        assertEquals(postTag2.getStatus(),PostTagStatus.UPDATED);
        assertEquals(postTag3.getStatus(),PostTagStatus.DELETED);
        assertEquals(newTagNames, result.getTagNames());
    }
    @Test
    @DisplayName("게시물 수정_기존 태그보다 더 추가")
    void updatePost_tagAdd() {
        // Given
        Long postId = 1L;
        List<String> tagNames = getTagNames4();
        PostUpdateDto postUpdateDto =PostUpdateDto.builder()
                .postId(postId)
                .title("new title")
                .content("new content")
                .tagNames(tagNames)
                .build();

        List<PostTag> postTags=new ArrayList<>();
        PostTag postTag1= PostTag.builder()
                .id(1L)
                .tagName("tag1")
                .status(PostTagStatus.UPDATED)
                .build();
        PostTag postTag2= PostTag.builder()
                .id(2L)
                .tagName("tag2")
                .status(PostTagStatus.UPDATED)
                .build();
        PostTag postTag3= PostTag.builder()
                .id(3L)
                .tagName("tag3")
                .status(PostTagStatus.UPDATED)
                .build();
        postTags.add(postTag1);
        postTags.add(postTag2);
        postTags.add(postTag3);
        Post post= Post.builder()
                .id(postId)
                .title("title")
                .content("content")
                .postTags(postTags)
                .build();
        given(postRepository.findByIdWithPostTag(postId)).willReturn(Optional.of(post));
        //When
        ResponsePostRegister result = postService.updatePost(postUpdateDto);
        List<String> newTagNames = post.getPostTags().stream().map(postTag -> postTag.getTagName())
                .collect(Collectors.toList());

        assertEquals(postId, result.getPostId());
        assertEquals(postUpdateDto.getTitle(), result.getTitle());
        assertEquals(postUpdateDto.getContent(), result.getContent());
        assertEquals(newTagNames, result.getTagNames());
    }

    @Test
    void getPostOne() {
    }

    @Test
    void deletePostAndMark() {
    }

    @Test
    void getUserPostsOrderByCreatedAt() {
    }

    @Test
    void getPosts() {
    }

    @Test
    void addViewCntToRedis() {
    }

    @Test
    void deleteViewCntCacheFromRedis() {
    }
}