package com.github.hippoom.food2go.infrastructure.persistence.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderRepository;
import com.github.hippoom.food2go.domain.model.order.TrackingId;

public class JpaPendingOrderRepositoryImpl implements PendingOrderRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	@Override
	public void store(PendingOrder pendingOrder) {
		entityManager.persist(pendingOrder);
	}

	@Override
	public TrackingId nextTrackingId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PendingOrder findBy(TrackingId trackingId) {
		return entityManager.find(PendingOrder.class, trackingId);
	}

}
