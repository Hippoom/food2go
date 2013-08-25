package com.github.hippoom.food2go.infrastructure.persistence.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;
import com.github.hippoom.food2go.interfaces.booking.facade.internal.transformer.RestaurantDetacher;

@Transactional(readOnly = true)
public class RestaurantDetacherImpl implements RestaurantDetacher {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Restaurant detach(Restaurant model) {
		Restaurant restaurant = entityManager.merge(model);
		restaurant.getMenuItems().size();// detach
		return restaurant;
	}

}
