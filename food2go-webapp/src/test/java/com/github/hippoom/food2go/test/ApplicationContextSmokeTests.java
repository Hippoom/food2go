package com.github.hippoom.food2go.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:f2g.xml" })
public class ApplicationContextSmokeTests implements IntegrationTests {
	@Test
	public void run() throws Exception {
		// this test aims to detect whether the who application context could be loaded
	}
}
