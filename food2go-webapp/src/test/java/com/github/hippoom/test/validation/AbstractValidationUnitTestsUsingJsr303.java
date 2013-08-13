package com.github.hippoom.test.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.PropertyValue;

import com.github.hippoom.food2go.test.UnitTests;

@RunWith(Parameterized.class)
public abstract class AbstractValidationUnitTestsUsingJsr303<T> implements
		UnitTests {

	protected PropertyValue[] propertyValues;
	protected Class<?> expected;

	public AbstractValidationUnitTestsUsingJsr303(Class<?> expected,
			PropertyValue[] propertyValues) {
		this.propertyValues = propertyValues;
		this.expected = expected;
	}

	public static Object[] itShould(Class<?> expected,
			PropertyValue... propertyValues) {
		Object[] parameters = new Object[2];

		PropertyValue[] pvs = propertyValues;

		parameters[0] = expected;
		parameters[1] = pvs;

		return parameters;
	}

	public static List<Object[]> itShould(Class<?> expected,
			List<PropertyValue> propertyValues) {
		List<Object[]> data = new ArrayList<Object[]>();

		for (PropertyValue propertyValue : propertyValues) {
			Object[] parameters = new Object[2];

			PropertyValue[] pvs = new PropertyValue[] { propertyValue };

			parameters[0] = expected;
			parameters[1] = pvs;

			data.add(parameters);
		}
		return data;
	}

	public static Class<?> pass() {
		return failFor(Passed.class);
	}

	public static Class<?> failFor(Class<?> expected) {
		return expected;
	}

	public static PropertyValue givenValid() {
		return new EmptyPropertyValue();
	}

	public static List<PropertyValue> givenBlank(String fieldName) {
		List<PropertyValue> pvs = new ArrayList<PropertyValue>();
		pvs.addAll(givenEmpty(fieldName));
		pvs.add(new PropertyValue(fieldName, " "));
		return pvs;
	}

	public static List<PropertyValue> givenEmpty(String fieldName) {
		List<PropertyValue> pvs = new ArrayList<PropertyValue>();
		pvs.add(new PropertyValue(fieldName, null));
		pvs.add(new PropertyValue(fieldName, ""));
		return pvs;
	}

	public static PropertyValue given(String fieldName, Object value) {
		return new PropertyValue(fieldName, value);
	}

	public static PropertyValue and(String fieldName, Object value) {
		return given(fieldName, value);
	}

	public @interface Passed {

	}

	@Test
	public void validates() {
		BeanWrapper wrapper = PropertyAccessorFactory
				.forBeanPropertyAccess(getTarget());
		for (int i = 0; i < propertyValues.length; i++) {
			if (propertyValues[i] instanceof EmptyPropertyValue) {
				continue;
			} else {
				wrapper.setPropertyValue(propertyValues[i]);
			}
		}
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> constraintViolations = validator
				.validate(getTarget());

		if (Passed.class.equals(expected)) {
			reportUnexpected(constraintViolations);
		} else {
			reportNoneExcpectConstraintViolationsFound(constraintViolations);
			reportTooMuch(constraintViolations);
			final ConstraintViolation<T> constraintViolation = theOnlyOneOf(constraintViolations);
			reportUnexpected(constraintViolation);
		}
	}

	private void reportUnexpected(
			Set<ConstraintViolation<T>> constraintViolations) {
		assertTrue("Unexpected constraint violation found, "
				+ constraintViolations.toString(),
				constraintViolations.isEmpty());
	}

	private void reportTooMuch(Set<ConstraintViolation<T>> constraintViolations) {
		assertEquals(constraintViolations.toString(), 1,
				constraintViolations.size());
	}

	private ConstraintViolation<T> theOnlyOneOf(
			Set<ConstraintViolation<T>> constraintViolations) {
		return constraintViolations.iterator().next();
	}

	private void reportUnexpected(
			final ConstraintViolation<T> constraintViolation) {
		assertEquals("Unexpected constraint violation found,", expected,
				constraintViolation.getConstraintDescriptor().getAnnotation()
						.annotationType());
	}

	private void reportNoneExcpectConstraintViolationsFound(
			Set<ConstraintViolation<T>> constraintViolations)
			throws AssertionError {
		if (constraintViolations.size() == 0) {
			throw new java.lang.AssertionError(
					"Expect constraint violations, got none. ");
		}
	}

	public abstract T getTarget();

	@Before
	public abstract void populatesTargetWithValidValues();

	@SuppressWarnings("serial")
	private static class EmptyPropertyValue extends PropertyValue {

		public EmptyPropertyValue() {
			super("givenValid", "givenValid");
			// TODO Auto-generated constructor stub
		}

	}

}