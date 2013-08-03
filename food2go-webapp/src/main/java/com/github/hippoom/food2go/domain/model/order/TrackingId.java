package com.github.hippoom.food2go.domain.model.order;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class TrackingId {
	private Long value;

	public TrackingId(Long value) {
		this.value = value;
	}
}
