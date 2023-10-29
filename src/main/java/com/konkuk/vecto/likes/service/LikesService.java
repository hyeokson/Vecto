package com.konkuk.vecto.likes.service;

import com.konkuk.vecto.feed.domain.Feed;
import com.konkuk.vecto.feed.repository.FeedRepository;
import com.konkuk.vecto.likes.repository.LikesRepository;
import com.konkuk.vecto.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    @Transactional
    public void saveLikes(Long feedId, String userId) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new IllegalArgumentException("좋아요를 누를 피드가 존재하지 않습니다."));
        feed.increaseLikeCount();
        likesRepository.insertLikes(feedId, userRepository.findByUserId(userId).orElseThrow().getId());
    }

    @Transactional
    public void deleteLikes(Long feedId, String userId) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new IllegalArgumentException("좋아요를 누를 피드가 존재하지 않습니다."));
        feed.decreaseLikeCount();
        likesRepository.deleteByFeedIdAndUserId(feedId, userRepository.findByUserId(userId).orElseThrow().getId());
    }
}
