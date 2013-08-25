package com.github.hippoom.food2go.domain.model.restaurant;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestaurantFixture {

	private RestaurantIdentity restaurantIdentity = new RestaurantIdentity(1);
	private List<MenuItem> menuItems = new ArrayList<MenuItem>() {
		{
			add(new MenuItem("Noodle", 10.00));
			add(new MenuItem("Rice", 11.00));
		}
	};

	public RestaurantFixture(RestaurantIdentity restaurantIdentity) {
		this.restaurantIdentity = restaurantIdentity;
	}

	public Restaurant build() {
		Restaurant restaurant = new Restaurant(restaurantIdentity,
				"Lan zhou noodle");
		restaurant.update(menuItems);
		return restaurant;
	}

}
