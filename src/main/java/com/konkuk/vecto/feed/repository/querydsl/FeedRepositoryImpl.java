package com.konkuk.vecto.feed.repository.querydsl;

import com.konkuk.vecto.feed.domain.*;
import com.konkuk.vecto.likes.domain.QLikes;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    QFeed feed = QFeed.feed;

    @Override
    public Optional<Feed> findByIdEager(Long feedId) {

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(feed)
                .where(feed.id.eq(feedId))
                .fetchOne());
    }
    @Override
    public List<Feed> findNextFeeds(Long nextFeedId, int limit) {


        return jpaQueryFactory
                .selectFrom(feed)
                .where(ltFeedId(feed, nextFeedId))
                .orderBy(feed.id.desc())
                .limit(limit)
                .fetch();
    }

    private BooleanExpression ltFeedId(QFeed feed, Long nextFeedId){
        return nextFeedId == null ? Expressions.booleanTemplate("true") : feed.id.lt(nextFeedId);
    }

    @Override
    public List<Feed> findNextFollowFeeds(Long nextFeedId, int limit, Long userId){

        QFollowFeed followFeed = QFollowFeed.followFeed;
        return jpaQueryFactory
                .select(followFeed.feed)
                .from(followFeed)
                .where(ltFeedId(followFeed.feed, nextFeedId)
                        .and(followFeed.userId.eq(userId))
                        .and(followFeed.feed.uploadTime.goe(LocalDateTime.now().minusDays(3))))
                .orderBy(followFeed.feed.id.desc())
                .limit(limit)
                .fetch();
    }


    @Override
    public List<Feed> findNextNotFollowFeed(Long nextFeedId, int limit, Long userId){

        // FollowFeed의 3일 이내의 게시글 id 조회 서브쿼리
        QFollowFeed followFeed = QFollowFeed.followFeed;

        JPQLQuery<Long> subQuery = JPAExpressions
                .select(followFeed.feed.id)
                .from(followFeed)
                .where(followFeed.userId.eq(userId)
                        .and(followFeed.feed.uploadTime.goe(LocalDateTime.now().minusDays(3))));


        return jpaQueryFactory
                .select(feed)
                .from(feed)
                .where(ltFeedId(feed, nextFeedId)
                        .and(feed.id.notIn(subQuery)))
                .orderBy(feed.id.desc())
                .limit(limit)
                .fetch();

    }

    @Override
    public List<Feed> findFeedByKeyWord(Long nextFeedId, int limit, String keyword){
        QFeedPlace feedPlace = QFeedPlace.feedPlace;
        return jpaQueryFactory
                .select(feedPlace.feed).distinct()
                .from(feedPlace)
                .where(ltFeedId(feedPlace.feed, nextFeedId)
                        .and((feedPlace.name.like("%"+keyword+"%"))
                                .or(feedPlace.address.like("%"+keyword+"%"))
                                .or(feedPlace.feed.title.like("%"+keyword+"%"))))
                .orderBy(feedPlace.feed.likeCount.desc(), feedPlace.feed.id.desc())
                .limit(limit)
                .fetch();
    }
    @Override
    public List<Feed> findNextLikesFeedByUserId(Long nextFeedId, int limit, String userId){
        QLikes likes = QLikes.likes;
        return jpaQueryFactory
                .select(likes.feed)
                .from(likes)
                .where(ltFeedId(likes.feed, nextFeedId).and(likes.user.userId.eq(userId)))
                .orderBy(likes.feed.id.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Feed> findNextFeedWrittenByUser(Long nextFeedId, int limit, String userId){

        return jpaQueryFactory
                .selectFrom(feed)
                .where(ltFeedId(feed, nextFeedId).and(feed.userId.eq(userId)))
                .orderBy(feed.id.desc())
                .limit(limit)
                .fetch();
    }


}
