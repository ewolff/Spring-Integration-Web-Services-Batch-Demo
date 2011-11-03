package com.ewolff.orderhandling.fullfillment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.ewolff.orderhandling.domain.Order;

@MessageEndpoint
public class FulFillment {

	private List<Order> orders = new ArrayList<Order>();

	@ServiceActivator(outputChannel = "processedorder", inputChannel = "normalfulfillment")
	public Order execute(Order order) {
		orders.add(order);
		return order;
	}

	public List<Order> getOrders() {
		return orders;
	}

}
