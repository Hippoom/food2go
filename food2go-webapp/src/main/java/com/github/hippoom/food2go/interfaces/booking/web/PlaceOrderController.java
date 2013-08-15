package com.github.hippoom.food2go.interfaces.booking.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@ModelAttribute("command")
	public PlaceOrderCommand command() {
		return new PlaceOrderCommand();
	}

	@RequestMapping(value = "/placeOrder", method = RequestMethod.GET)
	public String placeOrder(
			@ModelAttribute("command") PlaceOrderCommand command,
			ModelMap modelMap) {
		modelMap.put(BindingResult.MODEL_KEY_PREFIX + "command",
				modelMap.get("bindingResult"));
		return "placeOrder";
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
			@Valid @ModelAttribute("command") PlaceOrderCommand command,
			final BindingResult bindingResult, Model model,
			final RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return redirectToPlaceOrderGet(redirectAttributes, command,
					bindingResult);
		}
		try {
			final PendingOrder pendingOrder = placeOrderService.placeOrder(
					command.getDeliveryAddress(), command.getDeliveryTime());

			model.addAttribute("pendingOrder", pendingOrder);
			return "selectRestaurant";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return redirectToPlaceOrderGet(redirectAttributes, command,
					bindingResult);
		}
	}

	private String redirectToPlaceOrderGet(
			final RedirectAttributes redirectAttributes,
			PlaceOrderCommand command, final BindingResult bindingResult) {
		redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
		redirectAttributes.addFlashAttribute("command", command);
		return "redirect:/booking/placeOrder";
	}
}
