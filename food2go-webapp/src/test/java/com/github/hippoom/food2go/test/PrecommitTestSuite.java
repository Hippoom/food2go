package com.github.hippoom.food2go.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.github.hippoom.food2go.application.impl.TransactionalPlaceOrderServiceImplUnitTests;
import com.github.hippoom.food2go.interfaces.booking.web.PlaceOrderControllerIntegrationTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TransactionalPlaceOrderServiceImplUnitTests.class,
		PlaceOrderControllerIntegrationTests.class })
public class PrecommitTestSuite {

}
