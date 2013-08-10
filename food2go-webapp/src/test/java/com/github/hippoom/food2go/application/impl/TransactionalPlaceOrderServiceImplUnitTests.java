package com.github.hippoom.food2go.application.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderFactory;
import com.github.hippoom.food2go.domain.model.order.PendingOrderFixture;
import com.github.hippoom.food2go.domain.model.order.PendingOrderRepository;
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

	@Before
	public void inject() throws Exception {
		target.setPendingOrderRepository(pendingOrderRepository);
		target.setPendingOrderFactory(pendingOrderFactory);
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

}
