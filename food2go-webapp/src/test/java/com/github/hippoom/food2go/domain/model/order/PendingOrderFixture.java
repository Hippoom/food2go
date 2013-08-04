package com.github.hippoom.food2go.domain.model.order;

import java.util.Date;

public class PendingOrderFixture {
	private TrackingId trackingId = new TrackingId(1L);
	private Address deliveryAddress = new AddressFixture().build();
	private Date deliveryTime = new Date();

	public PendingOrderFixture with(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
		return this;
	}

	public PendingOrderFixture at(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
		return this;
	}

	public PendingOrder build() {
		return new PendingOrder(trackingId, deliveryAddress, deliveryTime);
	}

}
