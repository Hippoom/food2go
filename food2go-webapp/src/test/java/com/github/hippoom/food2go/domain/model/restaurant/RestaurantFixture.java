package com.github.hippoom.food2go.domain.model.restaurant;

public class RestaurantFixture {

	public Restaurant build() {
		Restaurant restaurant = new Restaurant(new RestaurantIdentity(1L),
				"Lan zhou noodle");
		return restaurant;
	}

}
