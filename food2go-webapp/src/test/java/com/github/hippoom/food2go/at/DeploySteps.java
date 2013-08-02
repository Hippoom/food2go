package com.github.hippoom.food2go.at;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeploySteps {

	private int statusCode;

	@Given("^the web application is \\(re\\)deployed$")
	public void the_web_application_is_re_deployed() throws Throwable {
		// assume that the web app is (re)deployed
	}

	@When("^I visit the application$")
	public void I_visit_the_application() throws Throwable {
		this.statusCode = getStatusCodeFromIndexPage();
	}

	private int getStatusCodeFromIndexPage() throws IOException,
			ClientProtocolException {
		return Request.Get("http://localhost:9999/food2go/index.jsp").execute()
				.returnResponse().getStatusLine().getStatusCode();
	}

	@Then("^a welcome page is shown$")
	public void a_welcome_page_is_shown() throws Throwable {
		assertThat(statusCode, equalTo(200));
	}
}
