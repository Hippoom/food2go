package com.github.hippoom.food2go.test;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.BaseTypeFilter;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@BaseTypeFilter(PersistenceTests.class)
public class PersistenceTestSuite {

}
