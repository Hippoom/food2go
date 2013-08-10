package com.github.hippoom.food2go.domain.model.restaurant;

import java.util.Date;

import com.github.hippoom.food2go.domain.model.order.Address;

public interface RestaurantRepository {

	boolean isAvailableFor(Address deliveryAddress, Date deliveryTime);

}
