package com.ewolff.orderhandling.fullfillment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ewolff.orderhandling.domain.Order;

@Component
public class ExpressFulFillmentJms {

	private List<Order> orders = new ArrayList<Order>();

	public Order execute(Order order) {
		orders.add(order);
		return order;
	}

	public List<Order> getOrders() {
		return orders;
	}

}
