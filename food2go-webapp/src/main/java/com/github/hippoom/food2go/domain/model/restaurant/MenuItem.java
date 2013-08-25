package com.github.hippoom.food2go.domain.model.restaurant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class MenuItem {
	@Getter
	private String name;
	@Getter
	private double price;

	public MenuItem(String name, double price) {
		this.name = name;
		this.price = price;
	}
}
