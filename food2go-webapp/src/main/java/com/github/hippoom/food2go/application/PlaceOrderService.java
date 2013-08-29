package com.github.hippoom.food2go.application;

import java.util.Date;
import java.util.List;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.order.OrderLine;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.TrackingId;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantIdentity;

public interface PlaceOrderService {

	PendingOrder placeOrder(Address deliveryAddress, Date deliveryTime);

	void update(TrackingId trackingId, RestaurantIdentity restaurantIdentity,
			List<OrderLine> orderLines);

}