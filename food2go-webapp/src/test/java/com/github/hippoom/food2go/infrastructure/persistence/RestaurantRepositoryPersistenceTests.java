package com.github.hippoom.food2go.infrastructure.persistence;

import static org.dbunit.operation.DatabaseOperation.DELETE_ALL;
import static org.dbunit.operation.DatabaseOperation.INSERT;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.CloseConnectionOperation;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;
import com.github.hippoom.food2go.test.PersistenceTests;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:context-infrastructure-persistence.xml" })
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

		final boolean result = repository.isAvailableFor(deliveryAddress,
				deliveryTime);

		assertThat(result, is(true));
	}

	@Test
	public void returnsAvailableWhenStreet2IsInServiceArea() throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = aStreet2MatchedAddress();
		final Date deliveryTime = anAvailableDeliveryTime();

		final boolean result = repository.isAvailableFor(deliveryAddress,
				deliveryTime);

		assertThat(result, is(true));
	}

	@Test
	public void returnsUnavailableWhenDeliveryAddressIsNotInServiceArea()
			throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = anUnavailableAddress();
		final Date deliveryTime = anAvailableDeliveryTime();

		final boolean result = repository.isAvailableFor(deliveryAddress,
				deliveryTime);

		assertThat(result, is(false));
	}

	@Test
	public void returnsUnavailableWhenDeliveryDateMismatched() throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = anAvailableDeliveryAddress();
		final Date deliveryTime = anUnavailableDeliveryDate();

		final boolean result = repository.isAvailableFor(deliveryAddress,
				deliveryTime);

		assertThat(result, is(false));
	}

	@Test
	public void returnsUnavailableWhenDeliveryTimeRangeTooEarly()
			throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = anAvailableDeliveryAddress();
		final Date deliveryTime = aTooEarlyDeliveryTimeRange();

		final boolean result = repository.isAvailableFor(deliveryAddress,
				deliveryTime);

		assertThat(result, is(false));
	}

	@Test
	public void returnsUnavailableWhenDeliveryTimeRangeTooLate()
			throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = anAvailableDeliveryAddress();
		final Date deliveryTime = aTooLateDeliveryTimeRange();

		final boolean result = repository.isAvailableFor(deliveryAddress,
				deliveryTime);

		assertThat(result, is(false));
	}

	@Test
	public void returnsAvailableWhenDeliveryTimeRangeMatchedLeftInterval()
			throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = anAvailableDeliveryAddress();
		final Date deliveryTime = aDeliveryTimeRangeMatchedLeftInterval();

		final boolean result = repository.isAvailableFor(deliveryAddress,
				deliveryTime);

		assertThat(result, is(true));
	}

	@Test
	public void returnsAvailableWhenDeliveryTimeRangeMatchedRightInterval()
			throws Exception {

		refreshAvailableRestaurants();

		final Address deliveryAddress = anAvailableDeliveryAddress();
		final Date deliveryTime = aDeliveryTimeRangeMatchedRightInterval();

		final boolean result = repository.isAvailableFor(deliveryAddress,
				deliveryTime);

		assertThat(result, is(true));
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
		String file = "classpath:t_f2g_restaurant_available.xml";
		deleteAll(file);// cannot use refresh because cannot set pk via
						// @CollectionTable
		insert(file);
	}

	private void deleteAll(String file) throws Exception {
		execute(DELETE_ALL, file);
	}

	private void insert(String file) throws Exception {
		execute(INSERT, file);
	}

	private void execute(DatabaseOperation refresh, String file)
			throws DatabaseUnitException, SQLException, MalformedURLException,
			DataSetException, IOException {
		new CloseConnectionOperation(refresh).execute(getConnection(),
				flatXmlDataSet(file(file)));
	}

	private File file(String file) throws IOException {
		return applicationContext.getResource(file).getFile();
	}

	private FlatXmlDataSet flatXmlDataSet(File file)
			throws MalformedURLException, DataSetException {
		return new FlatXmlDataSetBuilder().build(file);
	}

	private IDatabaseConnection getConnection() throws DatabaseUnitException,
			SQLException {
		return new DatabaseConnection(dataSource.getConnection());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
