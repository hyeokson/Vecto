package com.konkuk.vecto.feed.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.konkuk.vecto.likes.domain.Likes;
import org.junit.runner.Computer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	@Builder
	public Feed(String title, String content, LocalDateTime uploadTime, List<FeedMovement> feedMovements,
		List<FeedImage> feedImages, List<FeedPlace> feedPlaces) {
		this.title = title;
		this.content = content;
		this.uploadTime = uploadTime;
		setFeedMovements(feedMovements);
		setFeedImages(feedImages);
		setFeedPlaces(feedPlaces);

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

	private void setFeedMovements(List<FeedMovement> feedMovements) {
		this.feedMovements = feedMovements;
		for (FeedMovement feedMovement : feedMovements) {
			feedMovement.setFeed(this);
		}
	}

}
