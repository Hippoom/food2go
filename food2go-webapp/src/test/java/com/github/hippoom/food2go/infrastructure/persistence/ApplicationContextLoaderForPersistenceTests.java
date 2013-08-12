package com.github.hippoom.food2go.infrastructure.persistence;

import org.springframework.test.context.support.GenericXmlContextLoader;

public class ApplicationContextLoaderForPersistenceTests extends GenericXmlContextLoader {
	@Override
	protected String[] generateDefaultLocations(Class<?> clazz) {
		return new String[] { "classpath:context-infrastructure-persistence.xml" };
	}

	@Override
	protected String[] modifyLocations(Class<?> clazz, String... locations) {
		return locations;
	}
}