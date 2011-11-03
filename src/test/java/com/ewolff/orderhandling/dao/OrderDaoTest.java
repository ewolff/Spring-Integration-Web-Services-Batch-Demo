package com.ewolff.orderhandling.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ewolff.orderhandling.domain.Order;
import com.ewolff.orderhandling.domain.OrderItem;
import com.ewolff.orderhandling.test.AbstractSpringBasedTest;

public class OrderDaoTest extends AbstractSpringBasedTest {

	@Autowired
	public OrderDao orderDao;

	@Test 
	public void testCreateRead() {
		int numberOfOrder = orderDao.findAllOrder().size();
		Order order = new Order(false,21);
		order.addOrderItem(new OrderItem(42, "iPod"));
		Assert.assertEquals(0, order.getId());
		orderDao.create(order);
		Assert.assertEquals(numberOfOrder + 1, orderDao.findAllOrder().size());
		Order readOrder = orderDao.findById(order.getId());
		Assert.assertEquals(order, readOrder);
		Assert.assertEquals(1, readOrder.getOrderItem().size());
		Assert.assertTrue(order.getId() != 0);
	}
	
	@Test
	public void testCreateReadNoOrderItem() {
		int numberOfOrder = orderDao.findAllOrder().size();
		Order order = new Order(false,21);
		Assert.assertEquals(0, order.getId());
		orderDao.create(order);
		Assert.assertEquals(numberOfOrder + 1, orderDao.findAllOrder().size());
		Order readOrder = orderDao.findById(order.getId());
		Assert.assertEquals(order, readOrder);
		Assert.assertEquals(0, readOrder.getOrderItem().size());
		Assert.assertTrue(order.getId() != 0);
	}

	@Test 
	public void testUpdate() {
		Order order = new Order(false,21);
		order.addOrderItem(new OrderItem(42, "iPod"));
		orderDao.create(order);
		Assert.assertFalse(order.isProcessed());
		OrderItem orderItem = order.getOrderItem().get(0);
		orderItem.setCount(21);
		orderItem.setItem("MacBook");
		order.setProcessed(true);
		order.setExpress(true);
		order.setCustomer(20);
		orderDao.update(order);
		order = orderDao.findById(order.getId());
		Assert.assertTrue(order.isExpress());
		Assert.assertTrue(order.isProcessed());
		Assert.assertEquals(20, order.getCustomer());
		orderItem = order.getOrderItem().get(0);
		Assert.assertEquals(1, order.getOrderItem().size());
		Assert.assertEquals("MacBook", orderItem.getItem());
		Assert.assertEquals(21, orderItem.getCount());
	}

}
