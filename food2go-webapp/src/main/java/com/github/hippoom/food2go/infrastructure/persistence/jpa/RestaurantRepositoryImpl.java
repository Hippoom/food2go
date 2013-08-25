package com.github.hippoom.food2go.infrastructure.persistence.jpa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.transaction.annotation.Transactional;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantIdentity;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;
import com.github.hippoom.food2go.domain.model.restaurant.TimeRange;

@Transactional(readOnly = true)
public class RestaurantRepositoryImpl implements RestaurantRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public boolean isAvailableFor(Address deliveryAddress, Date deliveryTime) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
		Root<Restaurant> restaurants = query.from(Restaurant.class);

		does(query, restaurants, deliveryAddress, deliveryTime, criteriaBuilder);

		return entityManager.createQuery(
				query.select(criteriaBuilder.count(restaurants)))
				.getSingleResult() > 0;
	}

	@Override
	public List<Restaurant> findAvailableFor(Address deliveryAddress,
			Date deliveryTime) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Restaurant> query = criteriaBuilder
				.createQuery(Restaurant.class);
		Root<Restaurant> restaurants = query.from(Restaurant.class);

		does(query, restaurants, deliveryAddress, deliveryTime, criteriaBuilder);

		return entityManager.createQuery(
				query.orderBy(criteriaBuilder.asc(restaurants.get("name"))))
				.getResultList();
	}

	private <T> void does(CriteriaQuery<T> query, Root<Restaurant> restaurants,
			Address deliveryAddress, Date deliveryTime,
			CriteriaBuilder criteriaBuilder) {
		Subquery<Restaurant> serviceAreasFiltering = filterBy(criteriaBuilder,
				query.subquery(Restaurant.class), restaurants, deliveryAddress);

		Subquery<Restaurant> serviceTimeRangesFiltering = filterBy(
				criteriaBuilder, query.subquery(Restaurant.class), restaurants,
				deliveryTime);

		query.where(criteriaBuilder.and(
				criteriaBuilder.exists(serviceAreasFiltering),
				criteriaBuilder.exists(serviceTimeRangesFiltering)));
	}

	private Calendar with(Date deliveryTime) {
		Calendar c = Calendar.getInstance();
		c.setTime(deliveryTime);
		return c;
	}

	private String hhmmOf(Date deliveryTime) {
		return new SimpleDateFormat("HH:mm").format(deliveryTime);
	}

	private String getDayOfWeek(Calendar c) {
		return c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
	}

	private Subquery<Restaurant> filterBy(CriteriaBuilder criteriaBuilder,
			Subquery<Restaurant> subquery, Root<Restaurant> restaurants,
			Date deliveryTime) {
		ListJoin<Restaurant, TimeRange> serviceTimeRanges = subquery.correlate(
				restaurants).joinList("serviceTimeRanges");

		Expression<String> start = serviceTimeRanges.get("start");
		Expression<String> end = serviceTimeRanges.get("end");

		subquery.where(criteriaBuilder.and(
				criteriaBuilder.equal(serviceTimeRanges.get("day"),
						getDayOfWeek(with(deliveryTime))), criteriaBuilder
						.lessThanOrEqualTo(start, hhmmOf(deliveryTime)),
				criteriaBuilder.greaterThanOrEqualTo(end, hhmmOf(deliveryTime))));
		return subquery.select(restaurants);
	}

	private Subquery<Restaurant> filterBy(CriteriaBuilder criteriaBuilder,
			Subquery<Restaurant> subquery, Root<Restaurant> restaurants,
			Address deliveryAddress) {
		ListJoin<Restaurant, String> serviceAreas = subquery.correlate(
				restaurants).joinList("serviceAreas");
		subquery.where(criteriaBuilder.or(
				criteriaBuilder.equal(serviceAreas,
						deliveryAddress.getStreet1()),
				criteriaBuilder.equal(serviceAreas,
						deliveryAddress.getStreet2())));
		return subquery;
	}

	@Override
	public Restaurant findOne(RestaurantIdentity identity) {
		return entityManager.find(Restaurant.class, identity);
	}
}
