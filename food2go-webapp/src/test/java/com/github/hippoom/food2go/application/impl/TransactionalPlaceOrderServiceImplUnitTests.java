package com.github.hippoom.food2go.application.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.order.OrderLine;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderFactory;
import com.github.hippoom.food2go.domain.model.order.PendingOrderFixture;
import com.github.hippoom.food2go.domain.model.order.PendingOrderRepository;
import com.github.hippoom.food2go.domain.model.order.TrackingId;
import com.github.hippoom.food2go.domain.model.restaurant.MenuItem;
import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantFixture;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantIdentity;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;
import com.github.hippoom.food2go.test.UnitTests;

public class TransactionalPlaceOrderServiceImplUnitTests implements UnitTests {
	@Rule
	public final JUnitRuleMockery context = new JUnitRuleMockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	private TransactionalPlaceOrderServiceImpl target = new TransactionalPlaceOrderServiceImpl();
	@Mock
	private PendingOrderRepository pendingOrderRepository;
	@Mock
	private PendingOrderFactory pendingOrderFactory;
	@Mock
	private RestaurantRepository restaurantRepository;

	@Before
	public void inject() throws Exception {
		target.setPendingOrderRepository(pendingOrderRepository);
		target.setPendingOrderFactory(pendingOrderFactory);
		target.setRestaurantRepository(restaurantRepository);
	}

	@Test
	public void placesAPendingOrder() throws Exception {
		final PendingOrder pendingOrder = new PendingOrderFixture().build();
		final Address deliveryAddress = pendingOrder.getDeliveryAddress();
		final Date deliveryTime = pendingOrder.getDeliveryTime();

		context.checking(new Expectations() {
			{
				allowing(pendingOrderFactory).placeOrderWith(deliveryAddress,
						deliveryTime);
				will(returnValue(pendingOrder));

				oneOf(pendingOrderRepository).store(pendingOrder);
			}
		});

		PendingOrder order = target.placeOrder(deliveryAddress, deliveryTime);

		assertThat(order, is(pendingOrder));
	}

	@Test
	public void updatesRestaurantAndOrderLines() throws Exception {
		final PendingOrder pendingOrder = new PendingOrderFixture().build();
		final Restaurant restaurant = new RestaurantFixture().build();

		final TrackingId trackingId = pendingOrder.getTrackingId();
		final RestaurantIdentity restaurantId = restaurant.getId();
		final MenuItem menuItem = restaurant.getMenuItems().get(0);
		final List<OrderLine> orderLines = Arrays.asList(new OrderLine(menuItem
				.getName(), menuItem.getPrice(), 1));

		context.checking(new Expectations() {
			{
				allowing(pendingOrderRepository).findOne(trackingId);
				will(returnValue(pendingOrder));

				allowing(restaurantRepository).findOne(restaurantId);
				will(returnValue(restaurant));

				oneOf(pendingOrderRepository).store(pendingOrder);
			}
		});

		target.update(trackingId, restaurantId, orderLines);
		
		assertThat(pendingOrder.getRestaurantIdentity(), equalTo(restaurantId));
		assertThat(pendingOrder.getOrderLines(), equalTo(orderLines));
	}

}
