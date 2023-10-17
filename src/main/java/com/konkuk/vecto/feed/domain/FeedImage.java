package com.konkuk.vecto.feed.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feed_id")
	private Feed feed;

	private Long orderNum;

	private String url;

	public void setFeed(Feed feed) {
		this.feed = feed;
	}
	public FeedImage(Long index, String image) {
		this.orderNum = index;
		this.url = image;
	}
}
