package com.github.hippoom.food2go.domain.model.restaurant;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class TimeRange {
	private String day;
	private String start;
	private String end;
}
