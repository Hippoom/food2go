package com.github.hippoom.food2go.features;

import static org.dbunit.operation.DatabaseOperation.DELETE_ALL;
import static org.dbunit.operation.DatabaseOperation.INSERT;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.sql.DataSource;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.CloseConnectionOperation;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.order.PendingOrderFixture;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@Slf4j
public class PlaceOrderSteps implements ApplicationContextAware {
	@Setter
	private ApplicationContext applicationContext;
	@Autowired
	private DataSource dataSource;

	private Address deliveryAddress = PendingOrderFixture
			.defaultDeliveryAddressFixture().build();
	private String deliveryTime = nextSaturday(9, 00);
	private Content content;

	@Given("^restaurants ready$")
	public void restaurants_ready() throws Throwable {
		refreshAvailableRestaurants();
	}

	@When("^I submit delivery address and delivery time$")
	public void I_submit_delivery_address_and_delivery_time() throws Throwable {

		Response response = Request
				.Post("http://localhost:9999/food2go/booking/placeOrder")
				.bodyForm(
						param("deliveryAddressStreet1",
								deliveryAddress.getStreet1()),
						param("deliveryAddressStreet2",
								deliveryAddress.getStreet2()),
						param("deliveryTime", deliveryTime)).execute();
		content = response.returnContent();
		log.debug(content.toString());
	}

	private BasicNameValuePair param(String name, String value) {
		return new BasicNameValuePair(name, value);
	}

	@Then("^an pending order is placed$")
	public void an_pending_order_is_placed() throws Throwable {
		assertThat(content.asString(), containsString("TrackingId(value="));
	}

	@Given("^my delivery address is not in service area of any restaurant$")
	public void my_delivery_address_is_not_in_service_area_of_any_restaurant()
			throws Throwable {
		this.deliveryAddress = new Address("Mars", "Mercury");
	}

	@Then("^an pending order is not placed$")
	public void an_pending_order_is_not_placed() throws Throwable {
		assertThat(content.asString(),
				containsString("no restaurant available"));
	}

	@Given("^my delivery time is available of any restaurant$")
	public void my_delivery_time_is_available_of_any_restaurant()
			throws Throwable {
		this.deliveryTime = tomorrow(21, 00);
	}

	private String tomorrow(int hour, int min) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		set(c, hour, min);
		return format(c);
	}

	private String format(Calendar c) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(c.getTime());
	}

	private String nextSaturday(int hour, int min) {
		Calendar c = Calendar.getInstance();
		int weekday = c.get(Calendar.DAY_OF_WEEK);
		if (weekday != Calendar.SATURDAY) {
			// calculate how much to add
			// the 2 is the difference between Saturday and Monday
			int days = (Calendar.SATURDAY - weekday) % 7;
			c.add(Calendar.DAY_OF_YEAR, days);
		}
		set(c, hour, min);
		return format(c);
	}

	private void set(Calendar c, int hour, int min) {
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
	}

	private void refreshAvailableRestaurants() throws Exception {
		String file = "classpath:t_f2g_restaurant_place_order.xml";
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
}
