package com.blog.bloggy.post.service;


import com.blog.bloggy.comment.model.Comment;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
                .postId(post.getId())
                .userId(post.getPostUser().getUserId())
                .thumbnail(post.getThumbnail())
                .title(post.getTitle())
                .content(post.getContent())
                .tagNames(tags)
                .build();
    }

    private PostTag updatePostTag(Post post, String tagName) {
        return PostTag.updatePostTag(post,tagName);
    }

    private PostTag createPostTag(Post post, Tag tag,String tagName) {
        return PostTag.createPostTag(post, tag,tagName);
    }
    /*
        UPDATED: Tag 생성 전
        DELETED: 연관관계를 모두 지운, 삭제할 예정. (벌크 연산으로 한번에 삭제 예정)
        REGISTERED: 이미 Tag 객체가 존재하는 경우.
    */
    public ResponsePostRegister updatePost(PostUpdateDto postUpdateDto){
        Long postId = postUpdateDto.getPostId();
        Post post = postRepository.findByIdWithPostTag(postId)
                .orElseThrow(PostNotFoundException::new);
        String thumbnail = postUpdateDto.getThumbnail();
        String title = postUpdateDto.getTitle();
        String content = postUpdateDto.getContent();
        post.updatePost(thumbnail,title,content);
        //default batch size로 지연로딩 미리 초기화. (쿼리 테스트 before)
        List<Tag> init_tag = post.getPostTags().stream().map(postTag -> postTag.getTag()).collect(toList());
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
                        createPostTag(post, tag, tagName);
                    },
                    ()->{
                        updatePostTag(post, tagName);
                    }
            );
        }
        postTagRepository.saveAll(postTags);
        List<String> tags = postTags.stream().map((pt) -> pt.getTagName()).collect(toList());

        return ResponsePostRegister.builder()
                .postId(post.getId())
                .userId(postUpdateDto.getUserId())
                .title(post.getTitle())
                .content(post.getContent())
                .tagNames(tags)
                .build();
    }
    // 이 포스트를 클릭했을 때 Favorite을 누른 User는 하트가 색깔이 차있도록 보여야 한다.
    // 그렇다면 필요한 정보는? 현재 로그인한 User의 정보 Post가 보유한 Favorite중에 User에 대한 정보가 있는경우
    // isFavorite은 true로 반환. User는 로그인을 했을 수도 있고, 안했을 수도 있다.
    public ResponsePostOne getPostOne(Long postId,String username){
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(PostNotFoundException::new);
        boolean isFavorite=false;
        if(username!=null){
            List<String> usernames = favoriteRepository.getPostsFavoritesUsernames(postId);
            for(String favoriteName : usernames){
                if(username.equals(favoriteName))
                    isFavorite=true;
            }
        }
        ResponsePostOne responsePostOne= ResponsePostOne.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .name(post.getPostUser().getName())
                .isFavorite(isFavorite)
                .build();
        return responsePostOne;
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
