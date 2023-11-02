package com.konkuk.vecto.likes.service;

import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.repository.FeedRepository;
import com.konkuk.vecto.likes.repository.LikesRepository;
import com.konkuk.vecto.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.konkuk.vecto.likes.domain.Likes;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    @Transactional
    public void saveLikes(Long feedId, String userId) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR"));
        feed.increaseLikeCount();
        likesRepository.insertLikes(feedId, userRepository.findByUserId(userId).orElseThrow().getId());
    }

    @Transactional
    public void deleteLikes(Long feedId, String userId) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new IllegalArgumentException("FEED_NOT_FOUND_ERROR"));
        feed.decreaseLikeCount();
        likesRepository.deleteByFeedIdAndUserId(feedId, userRepository.findByUserId(userId).orElseThrow().getId());
    }

    public Boolean isClickedLikes(Long feedId, String userId) {
        Optional<Likes> likes =
                likesRepository.findByFeedIdAndUserId(feedId, userRepository.findByUserId(userId).orElseThrow().getId());
        return likes.isPresent();
    }
}
