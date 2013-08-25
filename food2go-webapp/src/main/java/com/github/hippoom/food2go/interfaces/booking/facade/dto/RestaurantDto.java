package com.github.hippoom.food2go.interfaces.booking.facade.dto;

import java.util.List;


import lombok.Data;

@Data
public class RestaurantDto {

	private String name;

	private List<MenuItemDto> menuItems;

}
