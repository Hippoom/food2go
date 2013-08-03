package com.github.hippoom.food2go.application.impl;

import java.util.Date;

import lombok.Setter;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderRepository;

public class TransactionalPlaceOrderServiceImpl {
	@Setter
	private PendingOrderRepository pendingOrderRepository;

	public PendingOrder placeOrder(Address deliveryAddress, Date deliveryTime) {
		PendingOrder pendingOrder = new PendingOrder(
				pendingOrderRepository.nextTrackingId(), deliveryAddress,
				deliveryTime);
		pendingOrderRepository.store(pendingOrder);
		return pendingOrder;
	}

}