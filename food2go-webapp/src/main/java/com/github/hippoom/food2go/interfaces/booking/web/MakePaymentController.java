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

@Controller
public class MakePaymentController {
	@Setter
	private PendingOrderRepository pendingOrderRepository;

	@RequestMapping(value = "/order/{trackingId}/payment", method = RequestMethod.GET)
	public String showMakePaymentForm(@PathVariable Long trackingId, Model model) {
		PendingOrder order = pendingOrderRepository.findOne(new TrackingId(
				trackingId));
		model.addAttribute("pendingOrder", order);
		return "payment";
	}

}
