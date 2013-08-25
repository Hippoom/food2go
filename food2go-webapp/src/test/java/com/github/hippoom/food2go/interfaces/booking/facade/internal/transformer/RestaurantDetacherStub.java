package com.github.hippoom.food2go.interfaces.booking.facade.internal.transformer;

import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;

public class RestaurantDetacherStub implements RestaurantDetacher {

	@Override
	public Restaurant detach(Restaurant model) {
		return model;
	}

}
