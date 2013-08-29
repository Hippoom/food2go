package com.github.hippoom.food2go.application.impl;

import java.util.Date;
import java.util.List;

import lombok.Setter;

import org.springframework.transaction.annotation.Transactional;

import com.github.hippoom.food2go.application.PlaceOrderService;
import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.order.OrderLine;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderFactory;
import com.github.hippoom.food2go.domain.model.order.PendingOrderRepository;
import com.github.hippoom.food2go.domain.model.order.TrackingId;
import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantIdentity;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;

@Transactional
public class TransactionalPlaceOrderServiceImpl implements PlaceOrderService {
	@Setter
	private PendingOrderRepository pendingOrderRepository;
	@Setter
	private PendingOrderFactory pendingOrderFactory;
	@Setter
	private RestaurantRepository restaurantRepository;

	@Override
	public PendingOrder placeOrder(Address deliveryAddress, Date deliveryTime) {
		PendingOrder pendingOrder = pendingOrderFactory.placeOrderWith(
				deliveryAddress, deliveryTime);
		pendingOrderRepository.store(pendingOrder);
		return pendingOrder;
	}

	@Override
	public void update(TrackingId trackingId,
			RestaurantIdentity restaurantIdentity, List<OrderLine> orderLines) {
		final PendingOrder order = pendingOrderRepository.findOne(trackingId);
		final Restaurant restaurant = restaurantRepository
				.findOne(restaurantIdentity);

		order.update(restaurant, orderLines);

		pendingOrderRepository.store(order);
	}

}
