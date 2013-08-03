package com.github.hippoom.food2go.domain.model.order;

import java.util.Date;

import lombok.Getter;

public class PendingOrder {
	@Getter
	private TrackingId trackingId;
	@Getter
	private Address deliveryAddress;
	@Getter
	private Date deliveryTime;

	public PendingOrder(TrackingId trackingId, Address deliveryAddress,
			Date deliveryTime) {
		this.trackingId = trackingId;
		this.deliveryAddress = deliveryAddress;
		this.deliveryTime = deliveryTime;
	}

}