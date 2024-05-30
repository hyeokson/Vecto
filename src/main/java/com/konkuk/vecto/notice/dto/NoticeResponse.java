package com.konkuk.vecto.notice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.konkuk.vecto.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class NoticeResponse {
    private Long id;

    private String title;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private LocalDateTime createdAt;

    public NoticeResponse(Notice notice){
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.createdAt = notice.getCreatedAt();
    }
}
