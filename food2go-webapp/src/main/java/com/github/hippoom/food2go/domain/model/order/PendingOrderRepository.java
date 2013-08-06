package com.github.hippoom.food2go.domain.model.order;

public interface PendingOrderRepository {

	void store(PendingOrder pendingOrder);

	TrackingId nextTrackingId();

	PendingOrder findBy(TrackingId trackingId);

}
