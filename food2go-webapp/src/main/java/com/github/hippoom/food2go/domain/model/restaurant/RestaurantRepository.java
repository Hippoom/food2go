package com.github.hippoom.food2go.domain.model.restaurant;

import java.util.Date;
import java.util.List;

import com.github.hippoom.food2go.domain.model.order.Address;

public interface RestaurantRepository {

	boolean isAvailableFor(Address deliveryAddress, Date deliveryTime);

	List<Restaurant> findAvailableFor(Address deliveryAddress, Date deliveryTime);

}
