package com.ewolff.orderhandling.webservice;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.transform.Source;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.transform.JDOMSource;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.integration.message.GenericMessage;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.XPathParam;
import org.w3c.dom.NodeList;

import com.ewolff.orderhandling.domain.Order;
import com.ewolff.orderhandling.domain.OrderItem;

@Endpoint
public class OrderEndpoint {

	private static Namespace namespace = Namespace.getNamespace("tns",
			"http://www.ewolff.com/order");
	private MessageChannel fulfillment;

	@Resource
	public void setFulfillment(MessageChannel fulfillment) {
		this.fulfillment = fulfillment;
	}

	@PayloadRoot(localPart = "orderRequest", namespace = "http://www.ewolff.com/order")
	public Source handleOrder(
			@XPathParam("/tns:orderRequest/tns:order/tns:order-item") NodeList orderItemNodeList,
			@XPathParam("/tns:orderRequest/tns:order/tns:express/text()") Boolean expressAsBoolean,
			@XPathParam("/tns:orderRequest/tns:order/tns:customer/text()") double customer) {
		boolean express = false;
		if (expressAsBoolean != null) {
			express = expressAsBoolean;
		}
		Order order = new Order(express, (int) customer);

		for (int i = 0; i < orderItemNodeList.getLength(); i++) {
			org.w3c.dom.Element element = (org.w3c.dom.Element) orderItemNodeList
					.item(i);
			int count = Integer.parseInt(element.getElementsByTagNameNS(
					"http://www.ewolff.com/order", "count").item(0)
					.getTextContent());
			String item = element.getElementsByTagNameNS(
					"http://www.ewolff.com/order", "item").item(0)
					.getTextContent();
			order.addOrderItem(new OrderItem(count, item));
		}

		RendezvousChannel rendezvousChannel = new RendezvousChannel();
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put(MessageHeaders.REPLY_CHANNEL, rendezvousChannel);
		GenericMessage<Order> message = new GenericMessage<Order>(order,
				headers);
		fulfillment.send(message);
		Element response = new Element("orderResponse", namespace);
		Element result = new Element("result", namespace);
		response.addContent(result);
		if (rendezvousChannel.receive(5000) == null) {
			result.setText("failure");
		} else {
			result.setText("success");
		}
		return new JDOMSource(response);
	}

}
