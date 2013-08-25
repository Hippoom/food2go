package com.github.hippoom.food2go.interfaces.booking.facade.internal.transformer;

import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;

public interface RestaurantDetacher {

	Restaurant detach(Restaurant model);

}
