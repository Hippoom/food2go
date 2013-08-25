package com.github.hippoom.food2go.domain.model.restaurant;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class RestaurantIdentity implements Serializable {
	@Getter
	private Long value;

	public RestaurantIdentity(Long value) {
		this.value = value;
	}

	public RestaurantIdentity(int value) {
		this(new Long(value));
	}
}
