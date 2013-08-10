package com.github.hippoom.food2go.interfaces.booking.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.hippoom.food2go.application.PlaceOrderService;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.interfaces.booking.web.command.PlaceOrderCommand;

@Slf4j
@Controller
public class PlaceOrderController {
	@Setter
	private PlaceOrderService placeOrderService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}

	/**
	 * <pre>
	 * A customer inputs deliveryAddress and deliveryTime 
	 * and a {@link PendingOrder} is placed, 
	 * then the customer is forwarded to a restaurant selection view.
	 * 
	 * </pre>
	 */
	@RequestMapping(value = "/placeOrder", method = RequestMethod.POST)
	public String placeOrder(
			@ModelAttribute("command") PlaceOrderCommand command, Model model) {

		try {
			final PendingOrder pendingOrder = placeOrderService.placeOrder(
					command.getDeliveryAddress(), command.getDeliveryTime());

			model.addAttribute("pendingOrder", pendingOrder);
			return "selectRestaurant";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("error", e.getMessage());
			return "placeOrder";
		}

	}
}
