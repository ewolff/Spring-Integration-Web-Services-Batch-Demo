package com.ewolff.orderhandling.fileparser;

import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.message.GenericMessage;
import org.springframework.stereotype.Component;

import com.ewolff.orderhandling.domain.Order;
import com.ewolff.orderhandling.domain.OrderItem;

@Component
public class FileParser {

	@Autowired
	private MessageChannel fulfillment;

	@ServiceActivator(inputChannel = "incomingfilecontent")
	public void handleFile(String content) {
		StringTokenizer stringTokenizer = new StringTokenizer(content, ",");
		while (stringTokenizer.hasMoreTokens()) {
			Order order = new Order(false, Integer.parseInt(stringTokenizer
					.nextToken()));
			order.addOrderItem(new OrderItem(Integer.parseInt(stringTokenizer
					.nextToken()), stringTokenizer.nextToken()));
			order.setExpress(stringTokenizer.nextToken().equalsIgnoreCase(
					"express"));
			GenericMessage<Order> orderMessage = new GenericMessage<Order>(
					order);
			fulfillment.send(orderMessage);
		}
	}


}
