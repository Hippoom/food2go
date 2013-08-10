package com.github.hippoom.food2go.interfaces.booking.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.github.hippoom.food2go.application.PlaceOrderService;
import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.order.AddressFixture;
import com.github.hippoom.food2go.domain.model.order.NoAvailableRestaurantException;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderFixture;
import com.github.hippoom.food2go.test.IntegrationTests;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/booking-servlet.xml",
		"classpath:test-booking-servlet.xml" })
@WebAppConfiguration
public class PlaceOrderControllerIntegrationTests implements IntegrationTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Autowired
	private PlaceOrderService placeOrderService;

	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(this.wac).build();

		reset(placeOrderService);// reset mock
	}

	@Test
	public void fowardsToFoodSelectionViewAfterPendingOrderIsPlaced()
			throws Exception {

		final Address deliveryAddress = new AddressFixture().build();
		final String deliveryTime = twoHoursLater();
		final PendingOrder pendingOrder = new PendingOrderFixture()
				.with(deliveryAddress).at(with(deliveryTime)).build();

		when(placeOrderService.placeOrder(deliveryAddress, with(deliveryTime)))
				.thenReturn(pendingOrder);

		mockMvc.perform(
				post("/placeOrder")
						.param("deliveryAddressStreet1",
								deliveryAddress.getStreet1())
						.param("deliveryAddressStreet2",
								deliveryAddress.getStreet2())
						.param("deliveryTime", deliveryTime))
				.andExpect(status().isOk())
				.andExpect(
						forwardedUrl("/WEB-INF/jsp/booking/selectRestaurant.jsp"))
				.andExpect(model().attribute("pendingOrder", pendingOrder));

	}

	@Test
	public void returnsToPlaceOrderViewWhenFailsToPlaceOrder() throws Exception {

		final Address deliveryAddress = new AddressFixture().build();
		final String deliveryTime = twoHoursLater();

		NoAvailableRestaurantException noAvailableRestaurantException = new NoAvailableRestaurantException(
				deliveryAddress, with(deliveryTime));
		when(placeOrderService.placeOrder(deliveryAddress, with(deliveryTime)))
				.thenThrow(noAvailableRestaurantException);

		mockMvc.perform(
				post("/placeOrder")
						.param("deliveryAddressStreet1",
								deliveryAddress.getStreet1())
						.param("deliveryAddressStreet2",
								deliveryAddress.getStreet2())
						.param("deliveryTime", deliveryTime))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/WEB-INF/jsp/booking/placeOrder.jsp"))
				.andExpect(
						model().attribute("error",
								noAvailableRestaurantException.getMessage()));

	}

	private String twoHoursLater() {
		return "2013-04-01 15:35";// test property editor, the time itself is
									// not very important in this test.
	}

	private Date with(String dateTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.parse(dateTime);
	}
}
