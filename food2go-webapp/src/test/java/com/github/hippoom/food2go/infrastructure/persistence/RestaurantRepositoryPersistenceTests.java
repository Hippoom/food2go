package com.github.hippoom.food2go.infrastructure.persistence;

import static com.github.hippoom.test.dbunit.DatabaseOperationBuilder.flatXml;
import static org.dbunit.operation.DatabaseOperation.DELETE_ALL;
import static org.dbunit.operation.DatabaseOperation.INSERT;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.restaurant.MenuItem;
import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantIdentity;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;
import com.github.hippoom.food2go.domain.model.restaurant.TimeRange;
import com.github.hippoom.food2go.test.PersistenceTests;
import com.github.hippoom.test.dbunit.DatabaseOperationBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = ApplicationContextLoaderForPersistenceTests.class)
public class RestaurantRepositoryPersistenceTests implements
		ApplicationContextAware, PersistenceTests {

	@Autowired
	private RestaurantRepository repository;
	@Autowired
	private DataSource dataSource;

	private ApplicationContext applicationContext;

	@Test
	public void returnsAvailableWhenStreet1IsInServiceArea() throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = aStreet1MatchedAddress();
		final Date deliveryTime = anAvailableDeliveryTime();

		final boolean available = repository.isAvailableFor(deliveryAddress,
				deliveryTime);
		final List<Restaurant> restaurants = repository.findAvailableFor(
				deliveryAddress, deliveryTime);

		thereShouldBe(available, restaurants);
	}

	private void thereShouldBe(final boolean available,
			final List<Restaurant> restaurants) {
		assertThat(available, is(true));
		assertThat(restaurants.size(), equalTo(2));
		assertThat(restaurants.get(0).getName(), lessThan(restaurants.get(1)
				.getName()));
	}

	@Test
	public void returnsAvailableWhenStreet2IsInServiceArea() throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = aStreet2MatchedAddress();
		final Date deliveryTime = anAvailableDeliveryTime();

		final boolean available = repository.isAvailableFor(deliveryAddress,
				deliveryTime);
		final List<Restaurant> restaurants = repository.findAvailableFor(
				deliveryAddress, deliveryTime);

		thereShouldBe(available, restaurants);
	}

	@Test
	public void returnsUnavailableWhenDeliveryAddressIsNotInServiceArea()
			throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = anUnavailableAddress();
		final Date deliveryTime = anAvailableDeliveryTime();

		final boolean available = repository.isAvailableFor(deliveryAddress,
				deliveryTime);
		final List<Restaurant> restaurants = repository.findAvailableFor(
				deliveryAddress, deliveryTime);

		thereShouldBeNo(available, restaurants);
	}

	@Test
	public void returnsUnavailableWhenDeliveryDateMismatched() throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = anAvailableDeliveryAddress();
		final Date deliveryTime = anUnavailableDeliveryDate();

		final boolean available = repository.isAvailableFor(deliveryAddress,
				deliveryTime);
		final List<Restaurant> restaurants = repository.findAvailableFor(
				deliveryAddress, deliveryTime);
		thereShouldBeNo(available, restaurants);
	}

	private void thereShouldBeNo(final boolean available,
			final List<Restaurant> restaurants) {
		assertThat(available, is(false));
		assertThat(restaurants, empty());
	}

	@Test
	public void returnsUnavailableWhenDeliveryTimeRangeTooEarly()
			throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = anAvailableDeliveryAddress();
		final Date deliveryTime = aTooEarlyDeliveryTimeRange();

		final boolean available = repository.isAvailableFor(deliveryAddress,
				deliveryTime);
		final List<Restaurant> restaurants = repository.findAvailableFor(
				deliveryAddress, deliveryTime);

		thereShouldBeNo(available, restaurants);
	}

	@Test
	public void returnsUnavailableWhenDeliveryTimeRangeTooLate()
			throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = anAvailableDeliveryAddress();
		final Date deliveryTime = aTooLateDeliveryTimeRange();

		final boolean available = repository.isAvailableFor(deliveryAddress,
				deliveryTime);
		final List<Restaurant> restaurants = repository.findAvailableFor(
				deliveryAddress, deliveryTime);

		thereShouldBeNo(available, restaurants);
	}

	@Test
	public void returnsAvailableWhenDeliveryTimeRangeMatchedLeftInterval()
			throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = anAvailableDeliveryAddress();
		final Date deliveryTime = aDeliveryTimeRangeMatchedLeftInterval();

		final boolean available = repository.isAvailableFor(deliveryAddress,
				deliveryTime);
		final List<Restaurant> restaurants = repository.findAvailableFor(
				deliveryAddress, deliveryTime);

		thereShouldBe(available, restaurants);
	}

	@Test
	public void returnsAvailableWhenDeliveryTimeRangeMatchedRightInterval()
			throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = anAvailableDeliveryAddress();
		final Date deliveryTime = aDeliveryTimeRangeMatchedRightInterval();

		final boolean available = repository.isAvailableFor(deliveryAddress,
				deliveryTime);
		final List<Restaurant> restaurants = repository.findAvailableFor(
				deliveryAddress, deliveryTime);
		thereShouldBe(available, restaurants);
	}

	/**
	 * <pre>
	 * respository.store() has not been added yet.
	 * I have to insert data using dbunit, this makes duplicate data in both java and xml
	 * 
	 * 
	 * </pre>
	 */
	@Transactional(readOnly = true)
	@Test
	public void findOne() throws Exception {

		refreshAvailableRestaurants();

		Restaurant restaurant = repository.findOne(new RestaurantIdentity(2));
		assertThat(restaurant.getName(), equalTo("Haidilao Hotpot"));
		assertThat(restaurant.getServiceAreas().size(), equalTo(3));
		assertThat(restaurant.getServiceAreas().get(0), equalTo("S1"));
		assertThat(restaurant.getServiceAreas().get(1), equalTo("S2"));
		assertThat(restaurant.getServiceAreas().get(2), equalTo("S3"));
		assertThat(restaurant.getServiceTimeRanges().size(), equalTo(2));
		assertThat(restaurant.getServiceTimeRanges().get(0),
				equalTo(new TimeRange("Sat", "09:00", "20:00")));
		assertThat(restaurant.getServiceTimeRanges().get(1),
				equalTo(new TimeRange("Sun", "09:00", "20:00")));
		assertThat(restaurant.getMenuItems().size(), equalTo(2));
		assertThat(restaurant.getMenuItems().get(0), equalTo(new MenuItem(
				"Fresh cabbage", 3.00)));
		assertThat(restaurant.getMenuItems().get(1), equalTo(new MenuItem(
				"Grilled beef", 20.00)));

	}

	private Address anUnavailableAddress() {
		return new Address("NOT_AVAILABLE", "NOT_AVAILABLE_EITHER");
	}

	private Address aStreet1MatchedAddress() {
		return new Address("S1", "NOT_AVAILABLE");// S1/S2/S3 available
	}

	private Address aStreet2MatchedAddress() {
		return new Address("NOT_AVAILABLE", "S2");// S1/S2/S3 available
	}

	private Date anAvailableDeliveryTime() throws ParseException {
		return nov(3, 2013, "13:00");// Sat/Sun available, this was a Sunday
	}

	private Date anUnavailableDeliveryDate() throws ParseException {
		return nov(4, 2013, "13:00");// Sat/Sun available, this was a Monday
	}

	private Date aTooEarlyDeliveryTimeRange() throws ParseException {
		return nov(3, 2013, "08:59");// 0900-2000
	}

	private Date aTooLateDeliveryTimeRange() throws ParseException {
		return nov(3, 2013, "20:01");// 0900-2000
	}

	private Date aDeliveryTimeRangeMatchedLeftInterval() throws ParseException {
		return nov(3, 2013, "09:00");// 0900-2000
	}

	private Date aDeliveryTimeRangeMatchedRightInterval() throws ParseException {
		return nov(3, 2013, "20:00");// 0900-2000
	}

	private Address anAvailableDeliveryAddress() {
		return new Address("S1", "S2");
	}

	private Date nov(int day, int year, String time) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(new SimpleDateFormat("HH:mm").parse(time));
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.NOVEMBER);
		c.set(Calendar.DAY_OF_MONTH, day);

		return c.getTime();
	}

	private void refreshAvailableRestaurants() throws Exception {
		String fixture = "classpath:t_f2g_restaurant_available.xml";
		// cannot use refresh because cannot set pk via @CollectionTable
		new DatabaseOperationBuilder(dataSource)
				.to(DELETE_ALL, flatXml(file(fixture)))
				.to(INSERT, flatXml(file(fixture))).execute();
	}

	private File file(String file) throws IOException {
		return applicationContext.getResource(file).getFile();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
