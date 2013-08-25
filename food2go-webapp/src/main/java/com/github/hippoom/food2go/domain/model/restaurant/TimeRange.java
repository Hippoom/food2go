package com.github.hippoom.food2go.domain.model.restaurant;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This is a value object.
 * 
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor
// for frameworks only
public class TimeRange {
	private String day;
	private String start;
	private String end;

	public TimeRange(String day, String start, String end) {
		this.day = day;
		this.start = start;
		this.end = end;
	}

}
