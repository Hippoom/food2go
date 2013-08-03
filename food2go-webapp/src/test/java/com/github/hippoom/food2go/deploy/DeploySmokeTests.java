package com.github.hippoom.food2go.deploy;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;

/**
 * This test runs after the web application is (re)deployed.
 * 
 */
@RunWith(Cucumber.class)
@Cucumber.Options(features = "classpath:deploy.feature", format = { "html:target/deploy-cucumber-html-report" })
public class DeploySmokeTests {

}
