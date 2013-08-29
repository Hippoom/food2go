package com.github.hippoom.food2go.domain.model.order;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * <pre>
 * Implements {@link Serializable} for using @EmbeddedId.
 * An alternative solution is using primitive type for @Id and
 * provide a trackingId() query method returning {@link TrackingId} for domain model
 * </pre>
 */
@SuppressWarnings("serial")
@ToString
@EqualsAndHashCode
public class TrackingId implements Serializable {
	@Getter
	private Long value;

	public TrackingId(Long value) {
		this.value = value;
	}

	/**
	 * for frameworks only
	 */
	public TrackingId() {
	}
}
