package com.github.hippoom.food2go.interfaces.booking.web;

import lombok.Setter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.hippoom.food2go.application.PlaceOrderService;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderRepository;
import com.github.hippoom.food2go.domain.model.order.TrackingId;
import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantIdentity;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;
import com.github.hippoom.food2go.interfaces.booking.facade.dto.RestaurantDto;
import com.github.hippoom.food2go.interfaces.booking.facade.internal.transformer.RestaurantDtoTransformer;
import com.github.hippoom.food2go.interfaces.booking.web.command.UpdateOrderLinesCommand;

@Controller
public class UpdateRestaurantController {
	@Setter
	private PendingOrderRepository pendingOrderRepository;
	@Setter
	private RestaurantRepository restaurantRepository;
	@Setter
	private RestaurantDtoTransformer restaurantDtoTransformer;
	@Setter
	private PlaceOrderService placeOrderService;

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

	@RequestMapping(value = "/restaurant/{restaurantId}", method = RequestMethod.GET)
	public @ResponseBody
	RestaurantDto displayRestaurant(@PathVariable Long restaurantId, Model model) {
		Restaurant restaurant = restaurantRepository
				.findOne(new RestaurantIdentity(restaurantId));
		return restaurantDtoTransformer.from(restaurant);
	}

	@RequestMapping(value = "/order/{trackingId}/updateOrderLines", method = RequestMethod.POST)
	public String updateOrderLines(@PathVariable Long trackingId,
			@ModelAttribute("command") UpdateOrderLinesCommand command,
			Model model, final RedirectAttributes redirectAttributes) {
		placeOrderService.update(new TrackingId(trackingId),
				command.restaurantIdentity(), command.orderLines());
		return "redirect:/booking/order/" + trackingId + "/payment";
	}
}
