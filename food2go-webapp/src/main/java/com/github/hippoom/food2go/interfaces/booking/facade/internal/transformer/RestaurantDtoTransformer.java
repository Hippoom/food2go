package com.github.hippoom.food2go.interfaces.booking.facade.internal.transformer;

import lombok.Setter;

import org.modelmapper.ModelMapper;

import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;
import com.github.hippoom.food2go.interfaces.booking.facade.dto.RestaurantDto;

public class RestaurantDtoTransformer {

	@Setter
	private RestaurantDetacher detacher;

	public RestaurantDto from(Restaurant model) {
		Restaurant restaurant = attachAndDetach(model);
		return new ModelMapper().map(restaurant, RestaurantDto.class);
	}

	protected Restaurant attachAndDetach(Restaurant model) {
		return detacher.detach(model);
	}
}