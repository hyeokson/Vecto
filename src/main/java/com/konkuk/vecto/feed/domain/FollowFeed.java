package com.konkuk.vecto.feed.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
	@Index(name = "idx_followfeed_user_id", columnList = "userId"),
		@Index(name = "idx_followfeed_created_at", columnList = "createdAt")
})
@EntityListeners(AuditingEntityListener.class)
public class FollowFeed {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feed_id")
	private Feed feed;

	private Long userId;

	@CreatedDate
	private LocalDateTime createdAt;

	public FollowFeed(Long userId, Feed feed) {
		this.userId = userId;
		this.feed = feed;
	}
}
