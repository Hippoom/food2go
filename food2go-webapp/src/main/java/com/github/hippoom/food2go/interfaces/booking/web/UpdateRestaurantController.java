package com.github.hippoom.food2go.interfaces.booking.web;

import lombok.Setter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderRepository;
import com.github.hippoom.food2go.domain.model.order.TrackingId;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;

@Controller
public class UpdateRestaurantController {
	@Setter
	private PendingOrderRepository pendingOrderRepository;
	@Setter
	private RestaurantRepository restaurantRepository;

	@RequestMapping(value = "/updateRestaurant/{trackingId}", method = RequestMethod.GET)
	public String updateRestaurant(@PathVariable Long trackingId, Model model) {
		PendingOrder order = pendingOrderRepository.findOne(new TrackingId(
				trackingId));
		model.addAttribute("pendingOrder", order);
		model.addAttribute(
				"restaurants",
				restaurantRepository.findAvailableFor(
						order.getDeliveryAddress(), order.getDeliveryTime()));
		return "updateRestaurant";
	}
}
