package com.blog.bloggy.post.service;


import com.blog.bloggy.comment.model.Comment;
import com.blog.bloggy.common.dto.TrendSearchCondition;
import com.blog.bloggy.common.exception.PostNotFoundException;
import com.blog.bloggy.common.exception.UserNotFoundException;
import com.blog.bloggy.common.repository.PagingQueryRepository;
import com.blog.bloggy.postTag.dto.PostTagStatus;
import com.blog.bloggy.comment.dto.CommentStatus;
import com.blog.bloggy.favorite.model.Favorite;
import com.blog.bloggy.post.dto.*;
import com.blog.bloggy.post.model.Post;
import com.blog.bloggy.comment.repository.CommentRepository;
import com.blog.bloggy.postTag.model.PostTag;
import com.blog.bloggy.favorite.repository.FavoriteRepository;
import com.blog.bloggy.post.repository.PostQueryRepository;
import com.blog.bloggy.post.repository.PostRepository;
import com.blog.bloggy.postTag.repository.PostTagRepository;
import com.blog.bloggy.tag.model.Tag;
import com.blog.bloggy.tag.repository.TagRepository;
import com.blog.bloggy.user.model.UserEntity;
import com.blog.bloggy.user.repository.UserRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.blog.bloggy.common.config.CacheKeyConfig.POST;
import static java.util.stream.Collectors.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostQueryRepository postQueryRepository;
    private final PagingQueryRepository pagingQueryRepository;
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final RedisTemplate redisTemplate;

    @Transactional
    public ResponsePostRegister createPost(PostDto postDto) {
        List<String> tagNames = postDto.getTagNames();
        UserEntity user = userRepository.findByUserId(postDto.getUserId())
                .orElseThrow(UserNotFoundException::new);
        Post post = Post.builder()
                .thumbnail(postDto.getThumbnail())
                .title(postDto.getTitle())
                .content(postDto.getTitle())
                .user(user)
                .build();
        postRepository.save(post);
        List<PostTag> postTags=new ArrayList<>();
        for (String tagName : tagNames) {
            tagRepository.findByName(tagName).ifPresentOrElse(
                    (tag)->{
                        postTags.add(createPostTag(post, tag, tagName));
                    },
                    ()->{
                        postTags.add(updatePostTag(post, tagName));
                    }
            );
        }
        postTagRepository.saveAll(postTags);
        List<String> tags = post.getPostTags().stream().map((pt) -> pt.getTagName()).collect(toList());
        return ResponsePostRegister.builder()
                .thumbnail(post.getThumbnail())
                .postId(post.getId())
                .thumbnail(post.getThumbnail())
                .title(post.getTitle())
                .content(post.getContent())
                .tagNames(tags)
                .build();
    }
    //@Transactional
    public PostTag updatePostTag(Post post, String tagName) {
        PostTag postTag=PostTag.builder()
                .tagName(tagName)
                .tagPost(post)
                .status(PostTagStatus.UPDATED)
                .build();

        post.addPostTag(postTag);
        return postTag;
    }
    //@Transactional
    public PostTag createPostTag(Post post, Tag tag,String tagName) {
        PostTag postTag = PostTag.builder()
                .tagPost(post)
                .tag(tag)
                .tagName(tagName)
                .status(PostTagStatus.REGISTERED)
                .build();
        post.addPostTag(postTag);
        tag.addPostTag(postTag);
        return postTag;
    }

    @CachePut(cacheNames = POST, key = "#post.getPostId()")
    public ResponsePost updatePost(PostUpdateDto post){
        String redisKey = postRedisKey(post.getPostId());
        // 업데이트된 정보 반환
        ResponsePost response = ResponsePost.builder()
                .thumbnail(post.getThumbnail())
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .tagNames(post.getTagNames())
                .modified(true)
                .build();
        log.info("redisKey : {}", redisKey);
        return response;
    }
    /*
        UPDATED: Tag 생성 전
        DELETED: 연관관계를 모두 지운, 삭제할 예정. (벌크 연산으로 한번에 삭제 예정)
        REGISTERED: 이미 Tag 객체가 존재하는 경우.
    */
    @Transactional(readOnly = true)
    public DataDto postsUpdate(List<ResponsePost> postDtos){
        DataDto dataDto=new DataDto();
        for (ResponsePost postDto : postDtos) {
            Long postId = postDto.getPostId();
            Post post = postRepository.findByIdWithPostTags(postId)
                    .orElseThrow(PostNotFoundException::new);
            String thumbnail = postDto.getThumbnail();
            String title = postDto.getTitle();
            String content = postDto.getContent();
            post.updatePost(thumbnail, title, content);
            List<PostTag> postTags = post.getPostTags();
            List<PostTagPair> newTags = postDto.getTagNames().stream().map(postTag -> PostTagPair.builder()
                    .tagName(postTag)
                    .status(PostTagStatus.UPDATED)
                    .build()).collect(toList());
            for (PostTag postTag : postTags) {
                if (postTag.getStatus() == PostTagStatus.DELETED)
                    continue;
                String oldTag = postTag.getTagName();
                Boolean check = false;
                for (PostTagPair newTag : newTags) {
                    if (oldTag.equals(newTag.tagName)) {
                        check = true;
                        newTag.setStatus(PostTagStatus.DELETED);
                    }
                }
                if (check == false) {
                    postTag.setStatus(PostTagStatus.DELETED);
                }
            }
            List<String> tags = newTags.stream()
                    .filter(pair -> pair.status != PostTagStatus.DELETED)
                    .map(pair -> pair.tagName)
                    .collect(toList());

            for (String tagName : tags) {
                tagRepository.findByName(tagName).ifPresentOrElse(
                        (tag) -> {
                            PostTag postTag = createPostTag(post, tag, tagName);
                            dataDto.tags.add(tag);
                            dataDto.postTags.add(postTag);
                        },
                        () -> {
                            PostTag postTag = updatePostTag(post, tagName);
                            dataDto.postTags.add(postTag);
                            log.info("postTag {}", postTag);
                        }
                );
            }
            dataDto.posts.add(post);
            log.info("updatePost = {}", post);
        }
        return dataDto;
    }

    @Scheduled(fixedDelay = 60000) // 일정 시간마다 실행될 수 있도록 스케줄링 설정 1000, 1초
    @Transactional
    public void saveUpdatedPostsToDB() {
        Set<String> redisKeys = redisTemplate.keys("POST::*");
        log.info("redisKeys : {}",redisTemplate.keys("POST::*"));
        List<ResponsePost> postsToUpdate = new ArrayList<>();
        if (redisKeys != null && !redisKeys.isEmpty()) {
            for (String redisKey : redisKeys) {
                log.info("redisKey == {}", redisKey);
                ResponsePost post = (ResponsePost) redisTemplate.opsForValue().get(redisKey);
                if (post != null && post.isModified()) { // 수정 여부를 확인하여 수정된 Post만 DB에 저장
                    log.info("ResponsePostDto == {} ", post);
                    postsToUpdate.add(post);
                }
            }
            //postRepository.saveAll(postsToUpdate);
            redisTemplate.delete(redisKeys);
        }
        DataDto dataDto = postsUpdate(postsToUpdate);
        if(!dataDto.posts.isEmpty()) {
            postRepository.saveAll(dataDto.getPosts());
        }
        if(!dataDto.postTags.isEmpty()) {
            postTagRepository.saveAll(dataDto.getPostTags());
        }
        if(!dataDto.tags.isEmpty()) {
            tagRepository.saveAll(dataDto.getTags());
        }
    }

    private String postRedisKey(Long postId) {
        return "POST::" + postId;
    }
    // 이 포스트를 클릭했을 때 Favorite을 누른 User는 하트가 색깔이 차있도록 보여야 한다.
    // 그렇다면 필요한 정보는? 현재 로그인한 User의 정보 Post가 보유한 Favorite중에 User에 대한 정보가 있는경우
    // isFavorite은 true로 반환. User는 로그인을 했을 수도 있고, 안했을 수도 있다.
    public ResponsePost getPostOne(Long postId, String username){
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(PostNotFoundException::new);
        boolean isFavorite=false;
        // 비로그인 상태면 확인할 필요 없음
        if(username==null) {
            Long usersId = userRepository.findIdByName(username).orElseThrow(UserNotFoundException::new);
            isFavorite=post.getFavorites().stream()
                    .anyMatch(favorite -> favorite.getFavoriteUser().getId() == usersId);
        }
        return getResponsePostOne(post);
    }

    @Cacheable(cacheNames = POST, key = "#postId")
    public ResponsePost getPostById(Long postId) {
        // DB에서 데이터를 조회하는 부분
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new PostNotFoundException());
        ResponsePost responsePost = getResponsePostOne(post);
        return responsePost; // 존재하지 않는 경우
    }
    @Transactional
    public ResponsePost getResponsePostOne(Post post) {
        ResponsePost responsePost = ResponsePost.builder()
                .postId(post.getId())
                .thumbnail(post.getThumbnail())
                .title(post.getTitle())
                .content(post.getContent())
                .name(post.getPostUser().getName())
                .tagNames(post.getRegPostTags().stream().map(postTag -> postTag.getTagName()).collect(toList()))
                .build();
        return responsePost;
    }

    public void deletePostAndMark(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        deletePostComments(post);
        commentRepository.deleteCommentsByPostId(postId);
        post.getFavorites().clear();
        favoriteRepository.deleteFavoritesByPostId(postId);
        postRepository.delete(post);
    }

    @Transactional
    public Page<ResponseUserPagePost> getUserPostsOrderByCreatedAt(String name, Pageable page){
        Long usersId = userRepository.findIdByName(name).orElseThrow(() -> new UserNotFoundException());
        return pagingQueryRepository.findUserPostsOrderByCreated(usersId,page);
    }

    public Slice<ResponsePostList> getPosts(Long lastId, Pageable pageable) {
        return pagingQueryRepository.findPostsForMain(lastId, pageable);
    }

    public Slice<ResponsePostList> getPostsOrderByTrend(TrendSearchCondition condition, Pageable pageable) {
        return pagingQueryRepository.findPostsForMainTrend(condition,pageable);
    }

    private static void deletePostFavorite(Post post) {
        Iterator<Favorite> iterator = post.getFavorites().iterator();
        //favorite 부분은 service와 연계해서 어떻게 구현할 지 고민 중 수정 예정
        while(iterator.hasNext()){
            Favorite favorite = iterator.next();
            favorite.getFavoritePost();
            iterator.remove();
        }
    }

    private static void deletePostComments(Post post) {
        Iterator<Comment> iterator = post.getComments().iterator();
        while(iterator.hasNext()){
            Comment comment = iterator.next();
            comment.setStatus(CommentStatus.DELETED);
            iterator.remove();
        }
    }

    public void addViewCntToRedis(Long postId) {
        String key = getKey(postId);
        //hint 캐시에 값이 없으면 레포지토리에서 조회 있으면 값을 증가시킨다.
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if(valueOperations.get(key)==null){
            valueOperations.set( key,
                    String.valueOf(postRepository.findById(postId)
                    .orElseThrow(PostNotFoundException::new).getViews()),
                    Duration.ofMinutes(10));
            valueOperations.increment(key);
        }
        /*
        else {
            valueOperations.increment(key); //만료시간 지나기 전까진 동일한 key에 대해 중복으로 increment 되지 않는다.
        }
        */
        log.info("value:{}",valueOperations.get(key));
    }

    private static String getKey(Long postId) {
        return "post:" + postId + ":views";
    }

    //3분마다 자동 실행해주는 스케쥴러

    //@Scheduled(cron = "0 0/3 * * * ?")
    public void deleteViewCntCacheFromRedis() {
        Set<String> redisKeys = redisTemplate.keys("post:*:views");
        Iterator<String> it = redisKeys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String numericPart = key.substring(key.indexOf(":") + 1, key.lastIndexOf(":"));
            Long postId = Long.parseLong(numericPart);
            String s = (String) redisTemplate.opsForValue().get(key);
            Long views = Long.parseLong(s);
            //
            postQueryRepository.addViewCntFromRedis(postId,views);
            redisTemplate.delete(key);
            redisTemplate.delete("post:"+postId+"views");
        }
    }
    @Data
    private static class PostTagPair{
        String tagName;

        PostTagStatus status;
        @Builder
        public PostTagPair(String tagName, PostTagStatus status) {
            this.tagName = tagName;
            this.status = status;
        }
    }
    @Data
    private static class DataDto{
        private List<Post> posts;
        private List<Tag> tags;
        private List<PostTag> postTags;
    }
}
