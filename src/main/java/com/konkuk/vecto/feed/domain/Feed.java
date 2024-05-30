package com.konkuk.vecto.feed.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.konkuk.vecto.comment.domain.Comment;
import com.konkuk.vecto.likes.domain.Likes;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
		@Index(name = "idx_feed_like_count", columnList = "likeCount"),
		@Index(name = "idx_feed_title", columnList = "title"),
		@Index(name = "idx_feed_user_id", columnList = "userId")
})
public class Feed {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String content;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime uploadTime;

	@OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
	private List<FeedMovement> feedMovements = new ArrayList<>();

	@OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
	private List<FeedImage> feedImages = new ArrayList<>();

	@OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
	private List<FeedPlace> feedPlaces = new ArrayList<>();

	@OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
	private List<Likes> likes = new ArrayList<>();

	@OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
	private List<FeedMapImage> feedMapImages = new ArrayList<>();

	private Integer likeCount;
	private String userId;

	@Builder
	public Feed(String title, String content, LocalDateTime uploadTime, List<FeedMovement> feedMovements,
		List<FeedImage> feedImages, List<FeedPlace> feedPlaces, String userId, List<FeedMapImage> feedMapImages) {
		this.title = title;
		this.content = content;
		this.uploadTime = uploadTime;
		this.userId = userId;
		this.likeCount = 0;
		setFeedMovements(feedMovements);
		setFeedImages(feedImages);
		setFeedPlaces(feedPlaces);
		setFeedMapImages(feedMapImages);
	}

	public void increaseLikeCount() {
		this.likeCount += 1;
	}

	public void decreaseLikeCount() {
		this.likeCount -= 1;
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	private void setFeedPlaces(List<FeedPlace> feedPlaces) {
		this.feedPlaces = feedPlaces;
		for (FeedPlace feedPlace : feedPlaces) {
			feedPlace.setFeed(this);
		}
	}

	private void setFeedImages(List<FeedImage> feedImages) {
		this.feedImages = feedImages;
		for (FeedImage feedImage : feedImages) {
			feedImage.setFeed(this);
		}
	}

	private void setFeedMapImages(List<FeedMapImage> feedMapImages) {
		this.feedMapImages = feedMapImages;
		for (FeedMapImage feedMapImage : feedMapImages) {
			feedMapImage.setFeed(this);
		}
	}

	private void setFeedMovements(List<FeedMovement> feedMovements) {
		this.feedMovements = feedMovements;
		for (FeedMovement feedMovement : feedMovements) {
			feedMovement.setFeed(this);
		}
	}

	public void patchFeed(String title, String content,
						  List<FeedImage> images, List<FeedMovement> movements, List<FeedPlace> places, List<FeedMapImage> mapImages) {
		this.title = title;
		this.content = content;
		setFeedImages(images);
		setFeedMovements(movements);
		setFeedPlaces(places);
		setFeedMapImages(mapImages);
	}
}
