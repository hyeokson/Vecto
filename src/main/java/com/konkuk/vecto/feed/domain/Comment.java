package com.konkuk.vecto.feed.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.konkuk.vecto.likes.domain.CommentLikes;
import com.konkuk.vecto.likes.domain.Likes;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feed_id")
	private Feed feed;

	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
	private List<CommentLikes> commentLikes = new ArrayList<>();

	private String userId;

	private String comment;

	private Integer likeCount;

	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime updatedAt;
	public Comment(Feed feed, String userId, String comment) {
		this.feed = feed;
		this.userId = userId;
		this.comment = comment;
		this.likeCount = 0;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}

}
