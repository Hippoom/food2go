package com.github.hippoom.food2go.infrastructure.persistence.jpa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;

@Transactional(readOnly = true)
public class RestaurantRepositoryImpl implements RestaurantRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public boolean isAvailableFor(Address deliveryAddress, Date deliveryTime) {
		//Use a native sql query because the sql is kind of complex
		//I believe that native sql is easier to read in this situation and hence easier to maintain.
		Calendar c = Calendar.getInstance();
		c.setTime(deliveryTime);
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

	private String hhmmOf(Date deliveryTime) {
		return new SimpleDateFormat("HH:mm").format(deliveryTime);
	}

	private String getDayOfWeek(Calendar c) {
		return c.getDisplayName(Calendar.DAY_OF_WEEK,
				Calendar.SHORT, Locale.US);
	}

}
