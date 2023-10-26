package com.konkuk.vecto.likes.service;

import com.konkuk.vecto.likes.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {
    LikesRepository likesRepository;

    @Transactional
    public void saveLikes(Long feedId, String userId) {
        likesRepository.insertLikes(feedId, userId);
    }

    @Transactional
    public void deleteLikes(Long feedId, String userId) {
        likesRepository.deleteByFeed_idAndUser_UserId(feedId, userId);
    }
}
