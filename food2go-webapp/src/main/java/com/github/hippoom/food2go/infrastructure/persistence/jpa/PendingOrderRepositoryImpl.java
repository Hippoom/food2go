package com.github.hippoom.food2go.infrastructure.persistence.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.TrackingId;
import com.github.hippoom.food2go.infrastructure.persistence.PendingOrderRepositoryCustom;

@Transactional(readOnly = true)
public class PendingOrderRepositoryImpl implements PendingOrderRepositoryCustom {
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	@Override
	public void store(PendingOrder pendingOrder) {
		entityManager.persist(pendingOrder);
	}

	@Override
	public TrackingId nextTrackingId() {
		Query query = entityManager
				.createNativeQuery("select seq_f2g_pending_order.nextval from dual");
		return new TrackingId(((Integer) query.getSingleResult()).longValue());
	}
}
