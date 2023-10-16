package com.konkuk.vecto.feed.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
	private final List<MovementCoordinate> movementCoordinates = new ArrayList<>();

	private Feed(List<MovementCoordinate> movementCoordinates) {
		for (MovementCoordinate movementCoordinate : movementCoordinates) {
			movementCoordinate.setFeed(this);
		}
		this.movementCoordinates.addAll(movementCoordinates);
	}

	public static Feed of(List<MovementCoordinate> movementCoordinates) {
		return new Feed(movementCoordinates);
	}
}
