package com.github.hippoom.food2go.interfaces.booking.web.command;

import java.util.Date;

import lombok.Data;

import com.github.hippoom.food2go.domain.model.order.Address;

/**
 * <pre>
 * There is no setter methods on {@link Address} , 
 * it's a ValueObject and therefore immutable.
 * The downside is duplicate code, 
 * let's say, another story is added to update deliveryAddress,
 * an UpdateDeliveryAddressCommand and a same getDeliveryAddress() are introduced.
 * So as the validation code. 
 * 
 * An alternative solution is use @RequestParam
 * and assemble {@link Address} in Controller.
 * But it's more difficult to add validation later.
 * 
 * 
 * </pre>
 */
@Data
public class PlaceOrderCommand {
	private String deliveryAddressStreet1;
	private String deliveryAddressStreet2;
	private Date deliveryTime;

	public Address getDeliveryAddress() {
		return new Address(deliveryAddressStreet1, deliveryAddressStreet2);
	}
}
