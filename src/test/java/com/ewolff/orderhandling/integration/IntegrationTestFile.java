package com.ewolff.orderhandling.integration;

import java.io.File;
import java.io.PrintWriter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ewolff.orderhandling.fullfillment.ExpressFulFillmentJms;
import com.ewolff.orderhandling.fullfillment.NormalFulFillment;
import com.ewolff.orderhandling.test.AbstractSpringBasedTest;

public class IntegrationTestFile extends AbstractSpringBasedTest {

	@Autowired
	private NormalFulFillment normalFulFillment;

	@Autowired
	private ExpressFulFillmentJms expressFulFillmentJms;

	@Test
	public void testNormalOrder() throws Exception {
		int fulFillmentBefore = normalFulFillment.getOrders().size();
		PrintWriter printWriter = new PrintWriter("/tmp/files/orders" + System.currentTimeMillis());
		printWriter.write("21,1,iPod,101,normal,");
		printWriter.flush();
		printWriter.close();
		int i = 0;
		while ((i < 50)
				&& normalFulFillment.getOrders().size() == fulFillmentBefore) {
			Thread.sleep(100);
			i++;
		}
		Assert.assertEquals(fulFillmentBefore + 1, normalFulFillment
				.getOrders().size());
	}

	@Test
	public void testExpressOrder() throws Exception {
		int fulFillmentBeforeJms = expressFulFillmentJms.getOrders().size();
		File file = new File("/tmp/files/orders" + System.currentTimeMillis());
		PrintWriter printWriter = new PrintWriter(file);
		printWriter.write("20,1,iPod,102,express,");
		printWriter.close();
		int i = 0;
		while (i < 70 && expressFulFillmentJms.getOrders().size() == fulFillmentBeforeJms) {
			Thread.sleep(100);
			i++;
		}
		Assert.assertEquals(fulFillmentBeforeJms + 1, expressFulFillmentJms
				.getOrders().size());
	}

}
