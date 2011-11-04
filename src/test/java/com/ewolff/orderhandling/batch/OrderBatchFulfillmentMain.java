package com.ewolff.orderhandling.batch;

import org.junit.Assert;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ewolff.orderhandling.dao.OrderDao;
import com.ewolff.orderhandling.domain.Order;
import com.ewolff.orderhandling.domain.OrderItem;

public class OrderBatchFulfillmentMain {
	
	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "/application-config.xml", "/jobs.xml",
						"/batch-config.xml" });
		OrderDao orderDao = (OrderDao) applicationContext.getBean("orderDao");
		JobLauncher jobLauncher = (JobLauncher) applicationContext
				.getBean("jobLauncher");
		Order order = new Order(false, 21);
		OrderItem orderItem = new OrderItem(0, "Power");
		order.addOrderItem(orderItem);
		for (int i = 0; i < 10; i++) {
			orderItem.setCount(i);
			orderDao.create(order);
			Assert.assertFalse(order.isProcessed());
		}
		Assert.assertTrue(orderDao.findAllOrder().size() > 9);
		JobExecution execution = jobLauncher.run((Job) applicationContext
				.getBean("fulfillmentjob"), new JobParameters());
		Assert.assertFalse(execution.isRunning());
		for (Order o : orderDao.findAllOrder()) {
			Assert.assertTrue(o.isProcessed());
			Assert.assertTrue(o.isInvoiced());
		}
		System.out.println("Sucess");
		System.exit(0);
	}
}
