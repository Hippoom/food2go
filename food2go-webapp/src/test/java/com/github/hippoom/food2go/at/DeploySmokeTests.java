package com.github.hippoom.food2go.at;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@Cucumber.Options(features = "classpath:deploy.feature", format = { "html:build/deploy-cucumber-html-report" })
public class DeploySmokeTests {

}
