package com.github.hippoom.food2go.interfaces.booking.web.command;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.github.hippoom.food2go.domain.model.order.OrderLine;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantIdentity;
import com.github.hippoom.food2go.interfaces.booking.facade.dto.OrderLineDto;

@Data
public class UpdateOrderLinesCommand {
	private Long restaurantId;
	private List<OrderLineDto> orderLines;

	public RestaurantIdentity restaurantIdentity() {
		return new RestaurantIdentity(restaurantId);
	}

	public List<OrderLine> orderLines() {
		List<OrderLine> result = new ArrayList<OrderLine>();
		for (OrderLineDto ol : orderLines) {
			result.add(new OrderLine(ol.getName(), ol.getPrice(), ol
					.getQuantity()));
		}
		return result;
	}

}
