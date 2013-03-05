package com.ewolff.orderhandling.fileparser;

import java.util.List;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ewolff.orderhandling.domain.Order;
import com.ewolff.orderhandling.domain.OrderItem;

public class FileParserTest {
	
	private FileParser fileParser;
	
	@Before
	public void setUp() {
		fileParser = new FileParser();
	}
	
	@Test
	public void express() {
		List<Order> result = fileParser.parse("21,1,iPod,101,normal,");
		assertEquals(1, result.size());
		Order order = result.get(0);
		assertEquals(21, order.getCustomer());
		assertEquals(false, order.isExpress());
		List<OrderItem> orderItemList = order.getOrderItem();
		assertEquals(1, orderItemList.size());
		OrderItem orderItem = orderItemList.get(0);
		assertEquals(1,orderItem.getCount());
		assertEquals(101,orderItem.getId());
		assertEquals("iPod",orderItem.getItem());
	}

}
