package com.github.hippoom.food2go.domain.model.restaurant;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class RestaurantIdentity implements Serializable {
	private Long value;

	public RestaurantIdentity(Long value) {
		this.value = value;
	}
}
