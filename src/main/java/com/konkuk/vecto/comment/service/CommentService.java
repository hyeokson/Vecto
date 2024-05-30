package com.konkuk.vecto.comment.service;

import com.konkuk.vecto.comment.domain.Comment;
import com.konkuk.vecto.comment.dto.request.CommentPatchRequest;
import com.konkuk.vecto.comment.dto.request.CommentRequest;
import com.konkuk.vecto.comment.dto.response.CommentsResponse;
import com.konkuk.vecto.comment.repository.CommentRepository;
import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.repository.FeedRepository;
import com.konkuk.vecto.global.util.TimeDifferenceCalculator;
import com.konkuk.vecto.likes.service.CommentLikesService;
import com.konkuk.vecto.user.dto.UserInfoResponse;
import com.konkuk.vecto.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final CommentLikesService commentLikesService;
    private final TimeDifferenceCalculator timeDifferenceCalculator;

    @Transactional
    public void saveComment(CommentRequest commentRequest, String userId) {
        Long feedId = commentRequest.getFeedId();
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR"));
        Comment comment = Comment.builder()
                .feed(feed)
                .userId(userId)
                .comment(commentRequest.getContent())
                .build();
        feed.addComment(comment);
    }

    @Transactional(readOnly = true)
    public CommentsResponse getFeedComments(Long nextCommentId, Long feedId, int limit, String userId) {
        if(!feedRepository.existsById(feedId))
            throw new IllegalArgumentException("FEED_NOT_FOUND_ERROR");


        List<Comment> comments = commentRepository.findNextComments(nextCommentId, feedId, limit+1);
        boolean isLastPage = comments.size()<=limit;
        if(comments.size()>limit)
            comments.remove(comments.size()-1);

        List<CommentsResponse.CommentResponse> commentResponses = comments
                .stream()
                .map(comment -> {
                    boolean likeFlag = false;
                    UserInfoResponse userInfo = userService.findUser(comment.getUserId());

                    if (userId != null) {
                        if (commentLikesService.isClickedLikes(comment.getId(), userId))
                            likeFlag = true;
                    }

                    return CommentsResponse.CommentResponse.builder()
                            .commentId(comment.getId())
                            .updatedBefore(!comment.getUpdatedAt().isEqual(comment.getCreatedAt()))
                            .nickName(userInfo.getNickName())
                            .userId(userInfo.getUserId())
                            .content(comment.getComment())
                            .timeDifference(timeDifferenceCalculator.formatTimeDifferenceKorean(comment.getCreatedAt()))
                            .profileUrl(userInfo.getProfileUrl())
                            .commentCount(comment.getLikeCount())
                            .likeFlag(likeFlag)
                            .build();
                })
                .toList();
        return CommentsResponse.builder()
                .isLastPage(isLastPage)
                .nextCommentId(isLastPage ? null : comments.get(limit-1).getId())
                .comments(commentResponses)
                .build();
    }

    @Transactional
    public void patchComment(CommentPatchRequest patchRequest, String userId) {
        Long commentId = patchRequest.getCommentId();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND_ERROR"));

        if (comment.getUserId().equals(userId)) {
            comment.setComment(patchRequest.getContent());
            return;
        }
        throw new IllegalArgumentException("COMMENT_CANNOT_DELETE_ERROR");
    }
    @Transactional
    public void deleteComment(Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND_ERROR"));
        if (comment.getUserId().equals(userId)) {
            commentRepository.deleteById(commentId);
            return;
        }
        throw new IllegalArgumentException("COMMENT_CANNOT_DELETE_ERROR");
    }

}
