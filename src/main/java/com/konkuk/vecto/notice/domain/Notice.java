package com.konkuk.vecto.notice.domain;

import com.konkuk.vecto.complaint.domain.ComplaintType;
import com.konkuk.vecto.global.common.entity.BaseEntity;
import com.konkuk.vecto.notice.dto.NoticeRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    public Notice(NoticeRequest noticeRequest){
        this.title = noticeRequest.getTitle();
        this.content = noticeRequest.getContent();
    }
}
