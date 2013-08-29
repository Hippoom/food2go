package com.github.hippoom.food2go.domain.model.order;

import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
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

import com.github.hippoom.food2go.domain.model.restaurant.Restaurant;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantIdentity;

@ToString(of = "trackingId")
@EqualsAndHashCode(of = "trackingId")
@NoArgsConstructor
// for frameworks only
@Entity
@Table(name = "t_f2g_pending_order")
/*
 * @SequenceGenerator(name = "seq_f2g_pending_order", sequenceName =
 * "seq_f2g_pending_order") this doesn't work, so I have to use /import.sql to
 * generate schema in persistence tests.
 */
public class PendingOrder {
	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "TRACKING_ID")) })
	@Getter
	private TrackingId trackingId;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "street1", column = @Column(name = "DELIVERY_ADDRESS_STREET1")),
			@AttributeOverride(name = "street2", column = @Column(name = "DELIVERY_ADDRESS_STREET2")) })
	@Getter
	private Address deliveryAddress;
	@Column(name = "delivery_Time")
	@Getter
	private Date deliveryTime;
	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "RESTAURANT_ID"))
	@Getter
	private RestaurantIdentity restaurantIdentity;
	@Getter
	@ElementCollection
	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @Column(name = "NAME")),
			@AttributeOverride(name = "price", column = @Column(name = "PRICE")),
			@AttributeOverride(name = "quantity", column = @Column(name = "QUANTITY")) })
	@CollectionTable(name = "t_f2g_order_line", joinColumns = @JoinColumn(name = "TRACKING_ID"))
	@OrderBy("name")
	private List<OrderLine> orderLines;

	/**
	 * This field is used to detect whether this {@link PendingOrder} needs save
	 * or update.
	 * 
	 * The default value is set to true(when this {@link PendingOrder} is
	 * retrieved from {@link PendingOrderRepository}).
	 * 
	 * When a {@link PendingOrder} is created via
	 * {@link PendingOrder#PendingOrder(TrackingId, Address, Date)}, this field
	 * is set to false, so
	 */

	public PendingOrder(TrackingId trackingId, Address deliveryAddress,
			Date deliveryTime) {
		this.trackingId = trackingId;
		this.deliveryAddress = deliveryAddress;
		this.deliveryTime = deliveryTime;
	}

	public void update(Restaurant restaurant, List<OrderLine> orderLines) {
		this.restaurantIdentity = restaurant.getId();
		this.orderLines = orderLines;
	}

}
