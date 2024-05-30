package com.konkuk.vecto.comment.repository.querydsl;

import com.konkuk.vecto.comment.domain.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findNextComments(Long nextCommentId,  Long feedId, int limit);
}
