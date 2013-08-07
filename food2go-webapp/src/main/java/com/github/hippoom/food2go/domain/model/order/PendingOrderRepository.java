package com.github.hippoom.food2go.domain.model.order;

import com.github.hippoom.food2go.infrastructure.persistence.PendingOrderRepositoryCustom;
import com.github.hippoom.food2go.infrastructure.persistence.QueryRepository;


public interface PendingOrderRepository extends
		QueryRepository<PendingOrder, TrackingId>, PendingOrderRepositoryCustom {

}
