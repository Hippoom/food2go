package com.github.hippoom.food2go.domain.model.order;

import java.util.Date;

import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;

import lombok.Setter;

public class PendingOrderFactory {
	@Setter
	private PendingOrderRepository pendingOrderRepository;
	@Setter
	private RestaurantRepository restaurantRepository;

	public PendingOrder placeOrderWith(Address deliveryAddress,
			Date deliveryTime) {

		if (restaurantRepository.isAvailableFor(deliveryAddress, deliveryTime)) {
			return new PendingOrder(pendingOrderRepository.nextTrackingId(),
					deliveryAddress, deliveryTime);
		} else {
			throw new NoAvailableRestaurantException(deliveryAddress,
					deliveryTime);
		}

	}
}
