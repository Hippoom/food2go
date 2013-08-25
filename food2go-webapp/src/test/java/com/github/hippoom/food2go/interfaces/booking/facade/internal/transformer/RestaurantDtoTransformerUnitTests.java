package com.github.hippoom.food2go.interfaces.booking.facade.internal.transformer;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantFixture;
import com.github.hippoom.food2go.interfaces.booking.facade.dto.RestaurantDto;
import com.github.hippoom.food2go.test.UnitTests;

public class RestaurantDtoTransformerUnitTests implements UnitTests {

	private RestaurantDtoTransformer transformer = new RestaurantDtoTransformer();

	@Before
	public void inject() {
		transformer.setDetacher(new RestaurantDetacherStub());
	}

	@Test
	public void testFromDomainModel() throws Exception {
		Restaurant model = new RestaurantFixture().build();
		RestaurantDto dto = transformer.from(model);

		assertThat(dto.getName(), equalTo(model.getName()));
		assertThat(dto.getMenuItems().size(), equalTo(2));
		assertThat(dto.getMenuItems().get(0).getName(), equalTo(model
				.getMenuItems().get(0).getName()));
		assertThat(dto.getMenuItems().get(0).getPrice(),
				equalTo(String.valueOf(model.getMenuItems().get(0).getPrice())));
		assertThat(dto.getMenuItems().get(1).getName(), equalTo(model
				.getMenuItems().get(1).getName()));
		assertThat(dto.getMenuItems().get(1).getPrice(),
				equalTo(String.valueOf(model.getMenuItems().get(1).getPrice())));
	}
}
