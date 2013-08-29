package com.github.hippoom.food2go.domain.model.restaurant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(of = { "id", "name" })
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
// for frameworks only
@Entity
@Table(name = "t_f2g_restaurant", uniqueConstraints = @UniqueConstraint(columnNames = { "NAME" }))
public class Restaurant {
	@Getter
	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "ID")) })
	private RestaurantIdentity id;
	@Getter
	@Column(name = "name")
	private String name;
	@Getter
	@ElementCollection
	@CollectionTable(name = "t_f2g_restaurant_srv_area", joinColumns = @JoinColumn(name = "RESTAURANT_ID"))
	@Column(name = "STREET")
	private List<String> serviceAreas = new ArrayList<String>();
	@Getter
	@ElementCollection
	@CollectionTable(name = "t_f2g_restaurant_srv_time", joinColumns = @JoinColumn(name = "RESTAURANT_ID"))
	@AttributeOverrides({
			@AttributeOverride(name = "day", column = @Column(name = "DAY")),
			@AttributeOverride(name = "start", column = @Column(name = "TIME_RANGE_START")),
			@AttributeOverride(name = "end", column = @Column(name = "TIME_RANGE_END")) })
	private List<TimeRange> serviceTimeRanges = new ArrayList<TimeRange>();
	@Getter
	@ElementCollection
	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @Column(name = "NAME")),
			@AttributeOverride(name = "price", column = @Column(name = "PRICE")) })
	@CollectionTable(name = "t_f2g_restaurant_menu_item", joinColumns = @JoinColumn(name = "RESTAURANT_ID"))
	@OrderBy("name")
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();

	public Restaurant(RestaurantIdentity id, String name) {
		this.id = id;
		this.name = name;
	}

	public void update(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
}
