package com.ewolff.orderhandling.fileparser;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.stereotype.Component;

import com.ewolff.orderhandling.domain.Order;
import com.ewolff.orderhandling.domain.OrderItem;

@Component
public class FileParser {

	public List<Order> parse(String content) {
		List<Order> result = new ArrayList<Order>();
		System.out.println("Content: " + content);
		StringTokenizer stringTokenizer = new StringTokenizer(content, ",");
		while (stringTokenizer.hasMoreTokens()) {
			Order order = new Order(false, Integer.parseInt(stringTokenizer
					.nextToken()));
			order.addOrderItem(new OrderItem(Integer.parseInt(stringTokenizer
					.nextToken()), stringTokenizer.nextToken(), Integer
					.parseInt(stringTokenizer.nextToken())));
			order.setExpress(stringTokenizer.nextToken().equalsIgnoreCase(
					"express"));
			result.add(order);
			System.out.println("Order out: " + order);
		}
		return result;
	}

}
