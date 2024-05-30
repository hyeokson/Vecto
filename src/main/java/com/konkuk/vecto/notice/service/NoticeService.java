package com.konkuk.vecto.notice.service;

import com.konkuk.vecto.notice.domain.Notice;
import com.konkuk.vecto.notice.dto.NoticeRequest;
import com.konkuk.vecto.notice.dto.NoticeResponse;
import com.konkuk.vecto.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public NoticeResponse getNotice(Long noticeId){
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("NOTICE_NOT_FOUND_ERROR"));
        return new NoticeResponse(notice);
    }


    @Transactional(readOnly = true)
    public List<NoticeResponse> getNoticeAll(){
        List<Notice> notices = noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return notices.stream()
                .map(NoticeResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public NoticeResponse getNoticeLatest(){
        Notice notice = noticeRepository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new IllegalArgumentException("NOTICE_NOT_FOUND_ERROR"));
        return new NoticeResponse(notice);
    }

    @Transactional
    public void saveNotice(NoticeRequest noticeRequest){
        noticeRepository.save(new Notice(noticeRequest));
    }

    @Transactional
    public void patchNotice(Long noticeId, NoticeRequest noticeRequest){
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("NOTICE_NOT_FOUND_ERROR"));
        if(noticeRequest.getTitle()!=null)
            notice.setTitle(noticeRequest.getTitle());
        if(noticeRequest.getContent()!=null)
            notice.setContent(noticeRequest.getContent());
    }

    @Transactional
    public void deleteNotice(Long noticeId){
        noticeRepository.deleteById(noticeId);
    }
}
