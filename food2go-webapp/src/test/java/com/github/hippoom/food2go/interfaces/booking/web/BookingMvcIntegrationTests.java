package com.github.hippoom.food2go.interfaces.booking.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
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
import com.github.hippoom.food2go.domain.model.order.OrderLine;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderFixture;
import com.github.hippoom.food2go.domain.model.order.PendingOrderRepository;
import com.github.hippoom.food2go.domain.model.order.TrackingId;
import com.github.hippoom.food2go.domain.model.restaurant.MenuItem;
import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantFixture;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantIdentity;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;
import com.github.hippoom.food2go.interfaces.booking.web.command.PlaceOrderCommand;
import com.github.hippoom.food2go.test.IntegrationTests;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/booking-servlet.xml",
		"classpath:context-interfaces-facade.xml",
		"classpath:test-booking-servlet.xml" })
@WebAppConfiguration
public class BookingMvcIntegrationTests implements IntegrationTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Autowired
	private PlaceOrderService placeOrderService;

	@Autowired
	private PendingOrderRepository pendingOrderRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;


	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(this.wac).build();

	}

	@After
	public void resetMocks() {
		reset(placeOrderService);
		reset(pendingOrderRepository);
		reset(restaurantRepository);
	}

	@Test
	public void showPlaceOrderForm() throws Exception {

		mockMvc.perform(get("/placeOrder"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("/WEB-INF/jsp/booking/placeOrder.jsp"))
				.andExpect(
						model().attribute("command", new PlaceOrderCommand()));

	}

	@Test
	public void showUpdateRetaurantForm() throws Exception {

		final PendingOrder pendingOrder = new PendingOrderFixture().build();
		final List<Restaurant> restaurants = Arrays
				.asList(new RestaurantFixture().build());

		when(pendingOrderRepository.findOne(pendingOrder.getTrackingId()))
				.thenReturn(pendingOrder);

		when(
				restaurantRepository.findAvailableFor(
						pendingOrder.getDeliveryAddress(),
						pendingOrder.getDeliveryTime()))
				.thenReturn(restaurants);

		mockMvc.perform(
				get("/updateRestaurant/"
						+ pendingOrder.getTrackingId().getValue()))
				.andExpect(status().isOk())
				.andExpect(
						forwardedUrl("/WEB-INF/jsp/booking/updateRestaurant.jsp"))
				.andExpect(model().attribute("pendingOrder", pendingOrder))
				.andExpect(model().attribute("restaurants", restaurants));

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
						.param("deliveryTime", deliveryTime)).andExpect(
				redirectedUrl("/booking/updateRestaurant/"
						+ pendingOrder.getTrackingId().getValue()));

	}

	@Test
	public void returnsToPlaceOrderViewWhenDomainLogicBroken() throws Exception {

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
				.andExpect(redirectedUrl("/booking/placeOrder"))
				.andExpect(flash().attributeExists("command"))
				.andExpect(
						flash().attribute("error",
								containsString("no restaurant available")));

	}

	@Test
	public void returnsToPlaceOrderViewWhenValidationBroken() throws Exception {

		final Address deliveryAddress = new AddressFixture().build();
		final String deliveryTime = null;

		mockMvc.perform(
				post("/placeOrder")
						.param("deliveryAddressStreet1",
								deliveryAddress.getStreet1())
						.param("deliveryAddressStreet2",
								deliveryAddress.getStreet2())
						.param("deliveryTime", deliveryTime))
				.andExpect(redirectedUrl("/booking/placeOrder"))
				.andExpect(flash().attributeExists("command", "bindingResult"));

	}

	@Test
	public void displayMenuItemsGivenRestaurantIdentity() throws Exception {

		RestaurantIdentity restaurantIdentity = new RestaurantIdentity(1);
		Restaurant restaurant = new RestaurantFixture(restaurantIdentity)
				.build();

		when(restaurantRepository.findOne(restaurantIdentity)).thenReturn(
				restaurant);

		mockMvc.perform(get("/restaurant/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("name").value(restaurant.getName()))
				.andExpect(
						jsonPath("menuItems.[0].name").value(
								restaurant.getMenuItems().get(0).getName()))
				.andExpect(
						jsonPath("menuItems.[0].price").value(
								String.valueOf(restaurant.getMenuItems().get(0)
										.getPrice())));

	}

	@Test
	public void redirectToPaymentViewAfterOrderLinesUpdated() throws Exception {

		Restaurant restaurant = new RestaurantFixture().build();

		MenuItem menuItem1 = restaurant.getMenuItems().get(0);
		MenuItem menuItem2 = restaurant.getMenuItems().get(1);

		mockMvc.perform(
				post("/order/1/updateOrderLines")
						.param("restaurantId",
								String.valueOf(restaurant.getId().getValue()))
						.param("orderLines[0].name", menuItem1.getName())
						.param("orderLines[0].price",
								String.valueOf(menuItem1.getPrice()))
						.param("orderLines[0].quantity", "1")
						.param("orderLines[1].name", menuItem2.getName())
						.param("orderLines[1].price",
								String.valueOf(menuItem2.getPrice()))
						.param("orderLines[1].quantity", "2")).andDo(print())
				.andExpect(redirectedUrl("/booking/order/1/payment"));

		verify(placeOrderService).update(
				new TrackingId(1L),
				restaurant.getId(),
				Arrays.asList(
						new OrderLine(menuItem1.getName(),
								menuItem1.getPrice(), 1), new OrderLine(
								menuItem2.getName(), menuItem2.getPrice(), 2)));
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
