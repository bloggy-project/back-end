package com.blog.bloggy.common.repository;

import com.blog.bloggy.common.dto.TrendSearchCondition;
import com.blog.bloggy.post.dto.*;
import com.blog.bloggy.post.model.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import static com.blog.bloggy.comment.model.QComment.comment;
import static com.blog.bloggy.favorite.model.QFavorite.favorite;
import static com.blog.bloggy.post.model.QPost.post;
import static com.blog.bloggy.user.model.QUserEntity.userEntity;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class PagingQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Slice<ResponsePostList> findPostsForMain(Long postId, Pageable pageable) {
        List<Long> ids = queryFactory
                .select(post.id)
                .from(post)
                .where(
                        ltPostId(postId)
                )
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();


        List<ResponsePostList> results = queryFactory
                .select(new QResponsePostList(
                        post.id,
                        post.thumbnail,
                        post.title,
                        post.content,
                        post.postUser.name,
                        post.createdAt,
                        post.commentCount,
                        post.favoriteCount
                ))
                .from(post)
                .where(
                        post.id.in(ids)
                )
                .orderBy(post.id.desc())
                .fetch();

        return checkLastPageDto(pageable, results);
    }

    public Slice<ResponsePostList> findPostsForMainTrend(TrendSearchCondition condition, Pageable pageable) {
        List<Long> ids = queryFactory.select(post.id)
                .from(post)
                .where(
                        mainTrendCondition(condition.getLastId(), condition.getFavorCount())
                )
                .orderBy(post.favoriteCount.desc())
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<ResponsePostList> posts = queryFactory
                .select(new QResponsePostList(
                        post.id,
                        post.thumbnail,
                        post.title,
                        post.content,
                        post.postUser.name,
                        post.createdAt,
                        post.commentCount,
                        post.favoriteCount
                ))
                .from(post)
                .join(post.postUser,userEntity)
                .where(
                        post.id.in(ids),
                        rangeDate(condition.getDate())
                )
                .fetch();
        return checkLastPageDto(pageable, posts);
    }


    @Transactional
    public Page<ResponseUserPagePost> findUserPostsOrderByCreated(Long userId, Pageable pageable) {
        List<Long> ids = queryFactory
                .select(post.id)
                .from(post)
                .where(post.postUser.id.eq(userId))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .where(post.id.in(ids))
                .orderBy(post.id.desc())
                .fetch();
        List<ResponseUserPagePost> results = posts.stream().map(post -> ResponseUserPagePost.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .tagNames(post.getRegPostTags().stream().map(postTag -> postTag.getTagName()).collect(toList()))
                .build()).collect(toList());
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.id.in(ids))
                .fetchOne();
        return new PageImpl<>(results, pageable, total);
    }


   //merge문제
    private static JPQLQuery<Long> getPostCommentCount() {
        return JPAExpressions
                .select(comment.count())
                .from(comment)
                .where(comment.commentPost.eq(post));
    }

    private static JPQLQuery<Long> getPostFavoriteCount() {
        return JPAExpressions
                .select(favorite.count())
                .from(favorite)
                .where(favorite.favoritePost.eq(post));
    }

    private Slice<Post> checkLastPage(Pageable pageable, List<Post> posts) {
        boolean hasNext=false;
        if(posts.size()> pageable.getPageSize()){
            hasNext=true;
            posts.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(posts,pageable,hasNext);
    }

    private Slice<ResponsePostList> checkLastPageDto(Pageable pageable, List<ResponsePostList> posts) {
        boolean hasNext=false;
        if(posts.size()> pageable.getPageSize()){
            hasNext=true;
            posts.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(posts,pageable,hasNext);
    }
    /*
     *  BooleanExpression 영역
     */

    private BooleanExpression usernameEq(String name) {
        return hasText(name) ? post.postUser.name.eq(name) : null;
    }

    /*
    private BooleanExpression usernameEq(String name) {
        return  hasText(name) ? post.username.eq(name) : null;
    }
    */

    private BooleanExpression createdAtExpression(LocalDateTime createdAt){
        if(createdAt==null)
            return null;
        return post.createdAt.gt(createdAt);
    }
    private BooleanExpression ltPostId(Long postId){
        if(postId==null)
            return null;
        return post.id.lt(postId);
    }
    private BooleanExpression ltFavorCount(Long count){
        if(count==null)
            return null;
        return post.favoriteCount.lt(count);
    }
    private BooleanExpression favorCountEq(Long count){
        if(count==null)
            return null;
        return post.favoriteCount.eq(count);
    }
    private BooleanExpression favorCountEqAndLtPostId(Long postId,Long count){
        BooleanExpression favorCountCondition = favorCountEq(count);
        BooleanExpression ltPostIdCondition = ltPostId(postId);

        if(favorCountCondition != null && ltPostIdCondition != null) {
            return (favorCountCondition)
                    .and(ltPostIdCondition);
        }
        if(favorCountCondition == null && ltPostIdCondition !=null){
            return ltPostIdCondition;
        }
        if(favorCountCondition!=null && ltPostIdCondition==null){
            return favorCountCondition;
        }
        return null;
    }
    private BooleanExpression mainTrendCondition(Long postId,Long count){
        BooleanExpression pfCondition = favorCountEqAndLtPostId(postId, count);
        BooleanExpression ltFavorCountCondition = ltFavorCount(count);
        if (pfCondition != null && ltFavorCountCondition != null) {
            return pfCondition.or(ltFavorCountCondition);
        }
        if (pfCondition == null && ltFavorCountCondition != null) {
            return ltFavorCountCondition;
        }
        if(pfCondition!=null && ltFavorCountCondition==null){
            return pfCondition;
        }
        return null;
    }



    private BooleanExpression rangeDate(String date){
        LocalDate now=LocalDate.now();
        if(date==null || date.equals("week")) {
            return post.createdAt.after(now.minusDays(7).atStartOfDay())
                    .and(post.createdAt.before(now.plusDays(1).atStartOfDay()));
        }
        if(date.equals("day")) {
            return post.createdAt.after(now.atStartOfDay())
                    .and(post.createdAt.before(now.plusDays(1).atStartOfDay()));
        }
        if(date.equals("month")) {
            return post.createdAt.after(now.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay())
                    .and(post.createdAt.before(now.plusMonths(1)
                            .with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay()));
        }
        if(date.equals("year")) {
            return post.createdAt.after(now.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay())
                    .and(post.createdAt.before(now.plusYears(1)
                            .with(TemporalAdjusters.firstDayOfYear()).atStartOfDay()));
        }
        return null;
    }


    // 검증이 끝난 최적화 이전, 미사용 쿼리

    /*
    @Transactional
    public Page<ResponseUserPagePost> findUserPostsOrderByCreatedOld(String name, Pageable pageable) {

        List<Long> ids = queryFactory
                .select(post.id)
                .from(post)
                .where(post.postUser.id.eq(
                        JPAExpressions.select(userEntity.id)
                                .from(userEntity)
                                .where(userEntity.name.eq(name))
                ))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .where(post.id.in(ids))
                .orderBy(post.id.desc())
                .fetch();
        List<ResponseUserPagePost> results = posts.stream().map(post -> ResponseUserPagePost.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .tagNames(post.getPostTags().stream().map(postTag -> postTag.getTagName()).collect(toList()))
                .build()).collect(toList());
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.id.in(ids))
                .fetchOne();
        return new PageImpl<>(results, pageable, total);
    }

    */

    /*
    public Page<Post> findUserPostsOrderByCreatedAt(String name, Pageable pageable) {
        Long userId = queryFactory.select(userEntity.id)
                .from(userEntity)
                .where(userEntity.name.eq(name)).fetchOne();
        List<Long> ids = queryFactory
                .select(post.id)
                .from(post)
                .where(post.postUser.id.eq(userId))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .where(post.id.in(ids))
                .orderBy(post.id.desc())
                .fetch();
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.id.in(ids))
                .fetchOne();
        return new PageImpl<>(posts, pageable, total);
    }

    */

    /*
    @Transactional
    public Page<ResponseUserPagePost> findUserPostsOrderByCreatedJoin(String name, Pageable pageable) {
        List<Long> ids = queryFactory
                .select(post.id)
                .from(post)
                .leftJoin(post.postUser, userEntity)
                .where(usernameEq(name))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .where(post.id.in(ids))
                .orderBy(post.id.desc())
                .fetch();
        List<ResponseUserPagePost> results = posts.stream().map(post -> ResponseUserPagePost.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .tagNames(post.getPostTags().stream().map(postTag -> postTag.getTagName()).collect(toList()))
                .build()).collect(toList());
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.id.in(ids))
                .fetchOne();
        return new PageImpl<>(results, pageable, total);
    }
    */

    /*
    @Transactional
    public Page<ResponseUserPagePost> findUserPostsOrderByCreatedFetchJoin(String name, Pageable pageable) {
        Long userId = queryFactory.select(userEntity.id)
                .from(userEntity)
                .where(userEntity.name.eq(name)).fetchOne();
        List<Long> ids = queryFactory
                .select(post.id)
                .from(post)
                .where(post.postUser.id.eq(userId))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .join(post.postTags, postTag).fetchJoin()
                .where(post.id.in(ids))
                .orderBy(post.id.desc())
                .fetch();
        List<ResponseUserPagePost> results = posts.stream().map(post -> ResponseUserPagePost.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .tagNames(post.getPostTags().stream().map(postTag -> postTag.getTagName()).collect(toList()))
                .build()).collect(toList());
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.id.in(ids))
                .fetchOne();
        return new PageImpl<>(results, pageable, total);
    }

    */

    /*
    @Transactional
    public Slice<ResponsePostList> findPostsForMainNotEagerAll(Long postId, Pageable pageable) {
        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .where(
                        ltPostId(postId)
                )
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        List<ResponsePostList> results = posts.stream().map(post -> ResponsePostList.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .username(post.getPostUser().getUserId())
                .createdAt(post.getCreatedAt())
                .commentCount(post.getComments().size())
                .favoriteCount(post.getFavorites().size())
                .build()).collect(toList());

        return checkLastPageDto(pageable, results);
    }
    */
}
