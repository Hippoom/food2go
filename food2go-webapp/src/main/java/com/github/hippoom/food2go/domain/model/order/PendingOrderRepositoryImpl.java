package com.github.hippoom.food2go.domain.model.order;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.github.hippoom.food2go.infrastructure.persistence.PendingOrderRepositoryCustom;

public class PendingOrderRepositoryImpl implements PendingOrderRepositoryCustom {
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	@Override
	public void store(PendingOrder pendingOrder) {
		entityManager.persist(pendingOrder);
	}

	// @Override
	public TrackingId nextTrackingId() {
		// TODO Auto-generated method stub
		return null;
	}

}
