package com.konkuk.vecto.likes.service;

import com.konkuk.vecto.feed.domain.Comment;
import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.repository.CommentRepository;
import com.konkuk.vecto.feed.repository.FeedRepository;
import com.konkuk.vecto.likes.domain.CommentLikes;
import com.konkuk.vecto.likes.domain.Likes;
import com.konkuk.vecto.likes.repository.CommentLikesRepository;
import com.konkuk.vecto.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikesService {
    private final CommentLikesRepository commentLikesRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean saveCommentLikes(Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND_ERROR"));
        if(!isClickedLikes(commentId, userId)){
            comment.increaseLikeCount();
            commentLikesRepository.insertCommentLikes(commentId, userRepository.findByUserId(userId).orElseThrow().getId());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteCommentLikes(Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND_ERROR"));
        if(isClickedLikes(commentId, userId)){
            comment.decreaseLikeCount();
            commentLikesRepository.deleteByCommentIdAndUserId(commentId, userRepository.findByUserId(userId).orElseThrow().getId());
            return true;
        }
        return false;
    }

    public Boolean isClickedLikes(Long commentId, String userId) {
        Optional<CommentLikes> commentLikes =
                commentLikesRepository.findByCommentIdAndUserId(commentId, userRepository.findByUserId(userId).orElseThrow().getId());
        return commentLikes.isPresent();
    }
}
