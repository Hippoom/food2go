package com.github.hippoom.food2go.features;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;

/**
 * </pre>
 * 
 * This test runs specific acceptance test by name given.
 * This test is not supposed to run in any test suite, 
 * it's just a convenient class for developing or debugging acceptance tests.
 * 
 * </pre>
 * 
 */
@RunWith(Cucumber.class)
@Cucumber.Options(features = { "classpath:features/." }, name = "Customer tries to place an pending order but failed for undeliverable address", format = { "html:target/acceptance-cucumber-html-report" })
public class SpecificAcceptanceTests {

}
