package com.github.hippoom.food2go.domain.model.order;

import java.util.Date;

@SuppressWarnings("serial")
public class NoAvailableRestaurantException extends RuntimeException {

	public NoAvailableRestaurantException(Address deliveryAddress,
			Date deliveryTime) {
		super("There is no restaurant available for " + deliveryAddress
				+ " and " + deliveryTime + ".");
	}

}
