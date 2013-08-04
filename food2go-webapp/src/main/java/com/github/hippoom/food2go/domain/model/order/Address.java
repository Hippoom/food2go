package com.github.hippoom.food2go.domain.model.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * <pre>
 * The address is simplified, city and state are removed.
 * 
 * Food2go is a small company at this moment, 
 * we don't have enough resources to handle trans-city business. :P
 * 
 * </pre>
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor
// @NoArgsConstructor for frameworks only
public class Address {
	@Getter
	private String street1;
	@Getter
	private String street2;

	public Address(String street1, String street2) {
		this.street1 = street1;
		this.street2 = street2;
	}

}
