package com.konkuk.vecto.comment.repository.querydsl;

import com.konkuk.vecto.comment.domain.Comment;
import com.konkuk.vecto.comment.domain.QComment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findNextComments(Long nextCommentId, Long feedId, int limit) {
        QComment comment = QComment.comment1;
        return jpaQueryFactory
                .selectFrom(comment)
                .where(ltCommentId(comment, nextCommentId).and(comment.feed.id.eq(feedId)))
                .orderBy(comment.id.desc())
                .limit(limit)
                .fetch();
    }

    private BooleanExpression ltCommentId(QComment comment, Long nextCommentId){
        return nextCommentId == null ? Expressions.booleanTemplate("true") : comment.id.lt(nextCommentId);
    }

}
