package com.github.hippoom.food2go.domain.model.order;

import static java.util.Calendar.HOUR;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;
import com.github.hippoom.food2go.test.UnitTests;

public class PendingOrderFactoryUnitTests implements UnitTests {
	@Rule
	public final JUnitRuleMockery context = new JUnitRuleMockery();
	@Rule
	public ExpectedException expectedExecption = ExpectedException.none();

	private PendingOrderFactory target = new PendingOrderFactory();
	@Mock
	private PendingOrderRepository pendingOrderRepository;
	@Mock
	private RestaurantRepository restaurantRepository;

	@Before
	public void inject() throws Exception {
		target.setPendingOrderRepository(pendingOrderRepository);
		target.setRestaurantRepository(restaurantRepository);
	}

	@Test
	public void placesAPendingOrder() throws Exception {
		final PendingOrder pendingOrder = new PendingOrderFixture().build();
		final Address deliveryAddress = pendingOrder.getDeliveryAddress();
		final Date deliveryTime = pendingOrder.getDeliveryTime();

		context.checking(new Expectations() {
			{
				allowing(restaurantRepository).isAvailableFor(deliveryAddress,
						deliveryTime);
				will(returnValue(true));

				allowing(pendingOrderRepository).nextTrackingId();
				will(returnValue(pendingOrder.getTrackingId()));
			}
		});

		PendingOrder order = target.placeOrderWith(deliveryAddress,
				deliveryTime);

		assertThat(order, is(pendingOrder));
	}

	@Test
	public void throwsExceptionWhenNoAvailableRestaurant() throws Exception {

		final Address deliveryAddress = new AddressFixture().build();
		final Date deliveryTime = twoHoursLater();

		context.checking(new Expectations() {
			{
				allowing(restaurantRepository).isAvailableFor(deliveryAddress,
						deliveryTime);
				will(returnValue(false));
			}
		});

		expectedExecption.expect(NoAvailableRestaurantException.class);
		expectedExecption.expectMessage("There is no restaurant available for "
				+ deliveryAddress + " and " + deliveryTime + ".");

		target.placeOrderWith(deliveryAddress, deliveryTime);
	}

	private Date twoHoursLater() {
		return PendingOrderFixture.after(2, HOUR);
	}
}
