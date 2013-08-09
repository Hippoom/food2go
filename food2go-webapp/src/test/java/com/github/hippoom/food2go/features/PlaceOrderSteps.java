package com.github.hippoom.food2go.features;

import static com.github.hippoom.food2go.domain.model.order.PendingOrderFixture.rightAfter;
import static java.util.Calendar.HOUR;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;

import com.github.hippoom.food2go.domain.model.order.Address;
import com.github.hippoom.food2go.domain.model.order.PendingOrderFixture;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@Slf4j
public class PlaceOrderSteps {
	private Response response;

	@When("^I submit delivery address and delivery time$")
	public void I_submit_delivery_address_and_delivery_time() throws Throwable {

		Address deliveryAddress = PendingOrderFixture
				.defaultDeliveryAddressFixture().build();

		response = Request
				.Post("http://localhost:9999/food2go/booking/placeOrder")
				.bodyForm(
						param("deliveryAddressStreet1",
								deliveryAddress.getStreet1()),
						param("deliveryAddressStreet2",
								deliveryAddress.getStreet2()),
						param("deliveryTime", rightAfter(2, HOUR))).execute();

	}

	private BasicNameValuePair param(String name, String value) {
		return new BasicNameValuePair(name, value);
	}

	@Then("^an pending order is placed$")
	public void an_pending_order_is_placed() throws Throwable {
		Content content = response.returnContent();
		log.debug(content.toString());
		assertThat(content.asString(), containsString("TrackingId(value="));
	}
}
