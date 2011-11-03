package com.ewolff.orderhandling.fullfillment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.ewolff.orderhandling.dao.OrderDao;
import com.ewolff.orderhandling.domain.Order;

@MessageEndpoint
public class NormalFulFillment {

	private OrderDao orderDao;

	@Autowired
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@ServiceActivator(inputChannel = "normalfulfillment")
	public Order execute(Order order) {
		orderDao.create(order);
		return order;
	}

	public List<Order> getOrders() {
		return orderDao.findAllOrder();
	}

}
