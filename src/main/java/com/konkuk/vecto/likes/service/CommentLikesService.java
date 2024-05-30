package com.konkuk.vecto.likes.service;

import com.konkuk.vecto.comment.repository.CommentRepository;
import com.konkuk.vecto.likes.domain.CommentLikes;
import com.konkuk.vecto.likes.repository.CommentLikesRepository;
import com.konkuk.vecto.user.repository.UserRepository;
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
        if(!isClickedLikes(commentId, userId)){
            commentRepository.increaseLikeCount(commentId);
            commentLikesRepository.insertCommentLikes(commentId, userRepository.findByUserId(userId).orElseThrow().getId());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteCommentLikes(Long commentId, String userId) {
        if(isClickedLikes(commentId, userId)){
            commentRepository.decreaseLikeCount(commentId);
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
