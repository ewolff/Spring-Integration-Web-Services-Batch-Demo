package com.ewolff.orderhandling.integration;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.message.GenericMessage;

import com.ewolff.orderhandling.domain.Order;
import com.ewolff.orderhandling.domain.OrderItem;
import com.ewolff.orderhandling.fullfillment.ExpressFulFillmentJms;
import com.ewolff.orderhandling.fullfillment.NormalFulFillment;
import com.ewolff.orderhandling.test.AbstractSpringBasedTest;

public class IntegrationTestChannel extends AbstractSpringBasedTest {

	@Resource
	private MessageChannel fulfillment;

	@Resource
	private MessageChannel normalfulfillment;

	@Autowired
	private NormalFulFillment normalFulFillment;

	@Autowired
	private ExpressFulFillmentJms expressFulFillmentJms;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNormalOrder() throws Exception {
		int fulFillmentBefore = normalFulFillment.getOrders().size();
		Order order = new Order(false,21);
		order.addOrderItem(new OrderItem(1,"iPod"));
		Message<Order> response = sendMessage(order, fulfillment);
		Assert.assertEquals(21, response.getPayload().getCustomer());
		Assert.assertEquals(fulFillmentBefore + 1, normalFulFillment.getOrders().size());
	}
	
	@Test
	public void testNormalOrderDirect() throws Exception {
		int fulFillmentBefore = normalFulFillment.getOrders().size();
		Order order = new Order(false,21);
		order.addOrderItem(new OrderItem(1,"iPod"));
		Message<Order> response = sendMessage(order, normalfulfillment);
		Assert.assertEquals(21, response.getPayload().getCustomer());
		Assert.assertEquals(fulFillmentBefore + 1, normalFulFillment.getOrders().size());
	}
	
	@Test
	public void testExpressOrder() throws Exception {
		int fulFillmentBeforeJms = expressFulFillmentJms.getOrders().size();
		Order order = new Order(true,22);
		order.addOrderItem(new OrderItem(1,"iPod"));
		Message<Order> response = sendMessage(order, fulfillment);
		Assert.assertEquals(22, response.getPayload().getCustomer());
		Assert.assertEquals(fulFillmentBeforeJms + 1, expressFulFillmentJms
				.getOrders().size());

	}

	private Message<Order> sendMessage(Order order, MessageChannel messageChannel) {
		Map<String, Object> headers = new HashMap<String, Object>();
		QueueChannel rendezvousChannel = new QueueChannel(10);
		headers.put(MessageHeaders.REPLY_CHANNEL, rendezvousChannel);
		GenericMessage<Order> message = new GenericMessage<Order>(order,
				headers);
		messageChannel.send(message);
		Message<Order> response = (Message<Order>) rendezvousChannel.receive(30000);
		return response;
	}


}
