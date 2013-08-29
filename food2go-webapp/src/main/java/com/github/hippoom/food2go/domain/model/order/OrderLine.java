package com.github.hippoom.food2go.domain.model.order;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class OrderLine {
	private String name;
	private double price;
	private int quantity;

	public OrderLine(String name, double price, int quantity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	/**
	 * for frameworks only
	 */
	public OrderLine() {
	}

	public OrderLine copy() {
		return new OrderLine(this.name, this.price, this.quantity);
	}
}
