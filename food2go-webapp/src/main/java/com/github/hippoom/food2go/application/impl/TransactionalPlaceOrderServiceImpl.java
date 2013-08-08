package com.github.hippoom.food2go.application.impl;

import java.util.Date;

import lombok.Setter;

import org.springframework.transaction.annotation.Transactional;

import com.github.hippoom.food2go.application.PlaceOrderService;
import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.infrastructure.persistence.PendingOrderRepositoryCustom;

public class TransactionalPlaceOrderServiceImpl implements PlaceOrderService {
	@Setter
	private PendingOrderRepositoryCustom pendingOrderRepository;
	
	@Transactional
	@Override
	public PendingOrder placeOrder(Address deliveryAddress, Date deliveryTime) {
		PendingOrder pendingOrder = new PendingOrder(
				pendingOrderRepository.nextTrackingId(), deliveryAddress,
				deliveryTime);
		pendingOrderRepository.store(pendingOrder);
		return pendingOrder;
	}

}
