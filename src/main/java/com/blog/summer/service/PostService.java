package com.blog.summer.service;


import com.blog.summer.domain.*;
import com.blog.summer.dto.PostTagStatus;
import com.blog.summer.dto.comment.CommentStatus;
import com.blog.summer.dto.post.*;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.*;
import com.blog.summer.repository.comment.CommentRepository;
import com.blog.summer.repository.favorite.FavoriteRepository;
import com.blog.summer.repository.post.PostQueryRepository;
import com.blog.summer.repository.post.PostRepository;
import com.blog.summer.repository.postTag.PostTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostQueryRepository postQueryRepository;
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final RedisTemplate redisTemplate;

    public ResponsePostRegister createPost(PostDto postDto) {
        List<String> tagNames = postDto.getTagNames();
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .categoryName(postDto.getCategoryName())
                .build();

        UserEntity user = userRepository.findByUserId(postDto.getUserId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));
        post.setPostUser(user);
        postRepository.save(post);
        List<PostTag> postTags=new ArrayList<>();
        for (String tagName : tagNames) {
            tagRepository.findByName(tagName).ifPresentOrElse(
                    (tag)->{
                        postTags.add(createPostTag(post, tag, tagName, PostTagStatus.REGISTERED));
                    },
                    ()->{
                        postTags.add(updatePostTag(post, tagName, PostTagStatus.UPDATED));
                    }
            );
        }
        Long postId=post.getId();
        String name=user.getName();

        postTagRepository.saveAll(postTags);
        List<String> tags = postTags.stream().map((pt) -> pt.getTagName()).collect(toList());

        return ResponsePostRegister.builder()
                .postId(postId)
                .userId(post.getPostUser().getUserId())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .categoryName(postDto.getCategoryName())
                .tagNames(tags)
                .build();
    }

    private PostTag updatePostTag(Post post, String tagName, PostTagStatus status) {
        return PostTag.updatePostTag(post,tagName,status);
    }

    private PostTag createPostTag(Post post, Tag tag,String tagName,PostTagStatus status) {
        return PostTag.createPostTag(post, tag,tagName,status);
    }
    /*
        UPDATED: Tag 생성 전
        DELETED: 연관관계를 모두 지운, 삭제할 예정. (벌크 연산으로 한번에 삭제 예정)
        REGISTERED: 이미 Tag 객체가 존재하는 경우.
    */
    public ResponsePostRegister updatePost(PostUpdateDto postUpdateDto){
        Long postId = postUpdateDto.getPostId();

        Post post = postRepository.findByIdWithPostTag(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        String title = postUpdateDto.getTitle();
        String content = postUpdateDto.getContent();
        post.updatePost(title,content);
        Iterator<PostTag> iterator = post.getPostTags().iterator();
        while(iterator.hasNext()){
            PostTag postTag = iterator.next();
            if(postTag.getStatus()==PostTagStatus.DELETED)
                continue;
            String oldName=postTag.getTagName();
            Boolean check=false;
            //일치하는 이름이 없는지 체크
            Iterator<String> iteratorNewNames =  postUpdateDto.getTagNames().iterator();
            while(iteratorNewNames.hasNext()){
                String newName = iteratorNewNames.next();
                if (oldName.equals(newName)) {
                    check=true;
                    iteratorNewNames.remove();
                }
            }
            if(check==false) {
                if (postTag.getStatus() == PostTagStatus.REGISTERED) {
                    //이 부분은 fetch join으로 최적화 할 수 있을 것 같음.
                    //추후 수정
                    Tag tag = postTag.getTag();
                    tag.removePostTag(postTag);
                    iterator.remove();
                    postTag.setStatus(PostTagStatus.DELETED);
                } else if (postTag.getStatus() == PostTagStatus.UPDATED) {
                    //UPDATED는 아직 Tag생성되지 않았으므로, 연관관계도 없음.
                    iterator.remove();
                    postTag.setStatus(PostTagStatus.DELETED);
                }
            }
        }
        List<String> tagNames = postUpdateDto.getTagNames();
        List<PostTag> postTags = post.getPostTags();
        for (String tagName : tagNames) {
            tagRepository.findByName(tagName).ifPresentOrElse(
                    (tag)->{
                        postTags.add(createPostTag(post, tag, tagName, PostTagStatus.REGISTERED));
                    },
                    ()->{
                        postTags.add(updatePostTag(post, tagName, PostTagStatus.UPDATED));
                    }
            );
        }
        postTagRepository.saveAll(postTags);
        List<String> tags = postTags.stream().map((pt) -> pt.getTagName()).collect(toList());

        return ResponsePostRegister.builder()
                .postId(postId)
                .userId(postUpdateDto.getUserId())
                .title(post.getTitle())
                .content(post.getContent())
                .categoryName(post.getCategoryName())
                .tagNames(tags)
                .build();
    }

    public ResponsePostOne getPostOne(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        ResponsePostOne responsePostOne= ResponsePostOne.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .name(post.getPostUser().getName())
                .categoryName(post.getCategoryName())
                .build();
        return responsePostOne;
    }

    public void deletePostAndMark(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        deletePostComments(post);
        commentRepository.deleteCommentsByPostId(postId);
        post.getFavorites().clear();
        favoriteRepository.deleteFavoritesByPostId(postId);
        postRepository.delete(post);
    }

    public Page<PostListDto> getPostAllByCreatedAt(Integer page,Integer size){
        PageRequest pageRequest=PageRequest.of(page, size, Sort.Direction.DESC,"createdAt");
        Page<Post> posts = postRepository.findPostsWithUsersAsPage(pageRequest);
        Page<PostListDto> toMap = posts.map(post -> PostListDto.builder()
                .title(post.getTitle())
                .categoryName(post.getCategoryName())
                .createdAt(post.getCreatedAt())
                .username(post.getPostUser().getName())
                .build());
        return toMap;
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
                    .orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다.")).getViews()),
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
    @Scheduled(cron = "0 0/3 * * * ?")
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



}
