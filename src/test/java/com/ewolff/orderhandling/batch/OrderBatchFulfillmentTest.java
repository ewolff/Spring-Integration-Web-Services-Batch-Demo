package com.ewolff.orderhandling.batch;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ewolff.orderhandling.dao.OrderDao;
import com.ewolff.orderhandling.domain.Order;
import com.ewolff.orderhandling.domain.OrderItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/application-config.xml", "/META-INF/spring/batch/jobs/jobs.xml",
		"/batch-config.xml" })
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class OrderBatchFulfillmentTest {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job fulfillmentjob;

	@Test
	public void testBatch() throws Exception {
		Order order = new Order(false, 21);
		OrderItem orderItem = new OrderItem(0, "Power");
		order.addOrderItem(orderItem);
		for (int i = 0; i < 10; i++) {
			orderItem.setCount(i);
			orderDao.create(order);
			Assert.assertFalse(order.isProcessed());
		}
		Assert.assertTrue(orderDao.findAllOrder().size() > 9);
		JobExecution execution = jobLauncher.run(fulfillmentjob,
				new JobParameters());
		Assert.assertFalse(execution.isRunning());
		for (Order o : orderDao.findAllOrder()) {
			Assert.assertTrue(o.isProcessed());
			Assert.assertTrue(o.isInvoiced());
		}
	}

}
