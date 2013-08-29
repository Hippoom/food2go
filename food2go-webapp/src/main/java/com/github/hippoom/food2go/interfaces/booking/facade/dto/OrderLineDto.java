package com.github.hippoom.food2go.interfaces.booking.facade.dto;

import lombok.Data;

@Data
public class OrderLineDto {
	private String name;
	private double price;
	private int quantity;
}
