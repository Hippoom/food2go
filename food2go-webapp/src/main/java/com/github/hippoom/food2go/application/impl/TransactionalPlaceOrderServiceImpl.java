package com.github.hippoom.food2go.application.impl;

import java.util.Date;

import lombok.Setter;

import org.springframework.transaction.annotation.Transactional;

import com.github.hippoom.food2go.application.PlaceOrderService;
import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderFactory;
import com.github.hippoom.food2go.domain.model.order.PendingOrderRepository;

public class TransactionalPlaceOrderServiceImpl implements PlaceOrderService {
	@Setter
	private PendingOrderRepository pendingOrderRepository;
	@Setter
	private PendingOrderFactory pendingOrderFactory;

	@Transactional
	@Override
	public PendingOrder placeOrder(Address deliveryAddress, Date deliveryTime) {
		PendingOrder pendingOrder = pendingOrderFactory.placeOrderWith(
				deliveryAddress, deliveryTime);
		pendingOrderRepository.store(pendingOrder);
		return pendingOrder;
	}

}
