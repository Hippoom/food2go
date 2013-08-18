package com.github.hippoom.food2go.infrastructure.persistence.jpa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.transaction.annotation.Transactional;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;
import com.github.hippoom.food2go.domain.model.restaurant.TimeRange;

@Transactional(readOnly = true)
public class RestaurantRepositoryImpl implements RestaurantRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public boolean isAvailableFor(Address deliveryAddress, Date deliveryTime) {
		// Use a native sql query because the sql is kind of complex
		// I believe that native sql is easier to read in this situation and
		// hence easier to maintain.
		Calendar c = with(deliveryTime);
		String sqlString = "select count(*) from t_f2g_restaurant r "
				+ "where exists (select * from t_f2g_restaurant_srv_area sa "
				+ "where (sa.street = ? or sa.street = ?) and r.id = sa.restaurant_id) "
				+ "and exists (select * from t_f2g_restaurant_srv_time st "
				+ "where st.day = ?  and (? between st.time_range_start and st.time_range_end) and r.id = st.restaurant_id) ";
		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter(1, deliveryAddress.getStreet1());
		query.setParameter(2, deliveryAddress.getStreet2());
		query.setParameter(3, getDayOfWeek(c));
		query.setParameter(4, hhmmOf(deliveryTime));
		return ((Number) query.getSingleResult()).longValue() > 0;
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

	@Override
	public List<Restaurant> findAvailableFor(Address deliveryAddress,
			Date deliveryTime) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Restaurant> query = criteriaBuilder
				.createQuery(Restaurant.class);
		Root<Restaurant> restaurants = query.from(Restaurant.class);

		Subquery<Restaurant> serviceAreasFiltering = filterBy(criteriaBuilder,
				query, restaurants, deliveryAddress);

		Subquery<Restaurant> serviceTimeRangesFiltering = filterBy(
				criteriaBuilder, query, restaurants, deliveryTime);

		query.where(criteriaBuilder.and(
				criteriaBuilder.exists(serviceAreasFiltering),
				criteriaBuilder.exists(serviceTimeRangesFiltering)));

		return entityManager.createQuery(query).getResultList();
	}

	private Subquery<Restaurant> filterBy(CriteriaBuilder criteriaBuilder,
			CriteriaQuery<Restaurant> query, Root<Restaurant> restaurants,
			Date deliveryTime) {
		Subquery<Restaurant> subquery = query.subquery(Restaurant.class);
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
			CriteriaQuery<Restaurant> query, Root<Restaurant> restaurants,
			Address deliveryAddress) {
		Subquery<Restaurant> subquery = query.subquery(Restaurant.class);
		ListJoin<Restaurant, String> serviceAreas = subquery.correlate(
				restaurants).joinList("serviceAreas");
		subquery.where(criteriaBuilder.or(
				criteriaBuilder.equal(serviceAreas,
						deliveryAddress.getStreet1()),
				criteriaBuilder.equal(serviceAreas,
						deliveryAddress.getStreet2())));
		return subquery;
	}
}
