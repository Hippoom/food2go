package com.github.hippoom.food2go.interfaces.booking.web.command;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.junit.Before;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.PropertyValue;

import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderFixture;
import com.github.hippoom.test.validation.AbstractValidationUnitTestsUsingJsr303;

public class PlaceOrderCommandUnitTests extends
		AbstractValidationUnitTestsUsingJsr303<PlaceOrderCommand> {
	public PlaceOrderCommand command;

	public PlaceOrderCommandUnitTests(Class<?> expected,
			PropertyValue[] propertyValues) {
		super(expected, propertyValues);
	}

	@Parameters
	public static Collection<Object[]> data() {
		Collection<Object[]> data = new ArrayList<Object[]>();

		data.add(itShould(pass(), givenValid()));
		data.addAll(itShould(failFor(NotBlank.class),
				givenBlank("deliveryAddressStreet1")));
		data.addAll(itShould(failFor(NotBlank.class),
				givenBlank("deliveryAddressStreet2")));
		data.add(itShould(failFor(NotNull.class), given("deliveryTime", null)));
		return data;
	}

	@Override
	public PlaceOrderCommand getTarget() {
		return command;
	}

	@Override
	@Before
	public void populatesTargetWithValidValues() {
		PendingOrder order = new PendingOrderFixture().build();

		command = new PlaceOrderCommand();
		command.setDeliveryAddressStreet1(order.getDeliveryAddress()
				.getStreet1());
		command.setDeliveryAddressStreet2(order.getDeliveryAddress()
				.getStreet2());
		command.setDeliveryTime(order.getDeliveryTime());
	}
}
