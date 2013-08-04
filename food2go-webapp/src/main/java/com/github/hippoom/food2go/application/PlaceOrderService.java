package com.github.hippoom.food2go.application;

import java.util.Date;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;

public interface PlaceOrderService {

	PendingOrder placeOrder(Address deliveryAddress, Date deliveryTime);

}