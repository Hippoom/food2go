package com.github.hippoom.food2go.application.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.order.AddressFixture;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderRepository;
import com.github.hippoom.food2go.domain.model.order.TrackingId;

public class TransactionalPlaceOrderServiceImplUnitTests {
	@Rule
	public final JUnitRuleMockery context = new JUnitRuleMockery();

	private TransactionalPlaceOrderServiceImpl target = new TransactionalPlaceOrderServiceImpl();
	@Mock
	private PendingOrderRepository pendingOrderRepository;

	@Before
	public void inject() throws Exception {
		target.setPendingOrderRepository(pendingOrderRepository);
	}

	@Test
	public void placesAPendingOrder() throws Exception {
		final TrackingId trackingId = new TrackingId(100L);
		final Address deliveryAddress = new AddressFixture().build();
		final Date deliveryTime = twoHoursLater();

		context.checking(new Expectations() {
			{
				allowing(pendingOrderRepository).nextTrackingId();
				will(returnValue(trackingId));

				oneOf(pendingOrderRepository).store(
						with(any(PendingOrder.class)));// cannot use explicit
														// matcher if the
														// PendingOrder is
														// created by the
														// application service
			}
		});

		PendingOrder order = target.placeOrder(deliveryAddress, deliveryTime);

		assertThat(order.getTrackingId(), equalTo(trackingId));
		assertThat(order.getDeliveryAddress(), equalTo(deliveryAddress));
		assertThat(order.getDeliveryTime(), equalTo(deliveryTime));
	}

	private Date twoHoursLater() {
		return new Date();// application layer does not handle deliveryTime
							// checking
	}
}
