package com.blog.bloggy.common.repository;

import com.blog.bloggy.post.dto.QResponsePostList;
import com.blog.bloggy.post.dto.ResponsePostList;
import com.blog.bloggy.post.dto.ResponseUserPagePostForLazy;
import com.blog.bloggy.post.model.Post;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static com.blog.bloggy.comment.model.QComment.comment;
import static com.blog.bloggy.favorite.model.QFavorite.favorite;
import static com.blog.bloggy.post.model.QPost.post;
import static com.blog.bloggy.postTag.model.QPostTag.postTag;
import static com.blog.bloggy.user.model.QUserEntity.userEntity;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class PagingQueryRepository {
    private final JPAQueryFactory queryFactory;


    public Slice<ResponsePostList> findPostsForMain(Long postId, Pageable pageable) {

        List<ResponsePostList> posts = queryFactory
                .select(new QResponsePostList(
                        post.id,
                        post.title,
                        post.content,
                        post.postUser.name,
                        post.createdAt,
                        getPostCommentCount(),
                        getPostFavoriteCount()
                ))
                .from(post)
                .where(
                        ltPostId(postId)
                )
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkLastPageDto(pageable, posts);
    }

    public Slice<ResponsePostList> findPostsForMainTrend(String date, Pageable pageable) {
        List<ResponsePostList> posts = queryFactory
                .select(new QResponsePostList(
                        post.id,
                        post.title,
                        post.content,
                        post.postUser.name,
                        post.createdAt,
                        getPostCommentCount(),
                        getPostFavoriteCount()
                ))
                .from(post)
                .where(
                        rangeDate(date)
                )
                .orderBy(Expressions.numberTemplate(Long.class,"{0}",JPAExpressions.select(favorite.count())
                                .from(favorite)
                                .where(favorite.favoritePost.eq(post))).desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkLastPageDto(pageable, posts);
    }

    public Page<Post> findUserPostsOrderByCreatedAtV2(String name, Pageable pageable) {
        // 커버링 인덱스 적용
        List<Long> ids = queryFactory
                .select(post.id)
                .from(post)
                .leftJoin(post.postUser, userEntity)
                .where(usernameEq(name))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .join(post.postTags,postTag).fetchJoin()
                .where(post.id.in(ids))
                .orderBy(post.createdAt.desc())
                .fetch();
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.id.in(ids))
                .fetchOne();
        return new PageImpl<>(posts, pageable, total);
    }

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
    private BooleanExpression rangeDate(String date){
        LocalDate now=LocalDate.now();
        if(date.equals("day")) {
            return post.createdAt.after(now.atStartOfDay())
                    .and(post.createdAt.before(now.plusDays(1).atStartOfDay()));
        }
        if(date.equals("week")) {
            return post.createdAt.after(now.minusDays(7).atStartOfDay())
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


    public Page<Post> findUserPagePostAllV1(String name, Pageable pageable) {
        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .join(post.postTags,postTag).fetchJoin()
                .leftJoin(post.postUser, userEntity)
                .where(
                        usernameEq(name)
                )
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.postUser, userEntity)
                .where(
                        usernameEq(name)
                )
                .fetchOne();
        return new PageImpl<>(posts, pageable, total);

    }

    public Page<ResponseUserPagePostForLazy> findUserPagePostAllV3(String name, Pageable pageable) {
        // 1) 커버링 인덱스로 대상 조회
        List<Long> ids = queryFactory
                .select(post.id)
                .from(post)
                .leftJoin(post.postUser, userEntity)
                .where(usernameEq(name))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<ResponseUserPagePostForLazy> posts = queryFactory
                .select(Projections.fields(ResponseUserPagePostForLazy.class,
                        post.id,
                        post.title,
                        post.content,
                        post.createdAt,
                        Projections.list(
                                post.postTags.as("postTags")
                        )
                ))
                .from(post)
                .join(post.postTags, postTag).fetchJoin()
                .where(post.id.in(ids))
                .orderBy(post.createdAt.desc())
                .fetch();
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.id.in(ids))
                .fetchOne();
        return new PageImpl<>(posts, pageable, total);
    }

}
