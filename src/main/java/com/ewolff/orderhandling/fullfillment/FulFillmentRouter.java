package com.ewolff.orderhandling.fullfillment;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;

import com.ewolff.orderhandling.domain.Order;

@MessageEndpoint
public class FulFillmentRouter {
	
	@Router(inputChannel="fulfillmentRouter")
	public String routeOrder(Order order) {
		if (order.isExpress()) {
			return "expressfulfillment";
		} else {
			return "normalfulfillment";
		}
	}

}
