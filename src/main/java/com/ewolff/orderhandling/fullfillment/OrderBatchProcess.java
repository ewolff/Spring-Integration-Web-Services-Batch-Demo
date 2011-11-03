package com.ewolff.orderhandling.fullfillment;

import java.util.List;

import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.ewolff.orderhandling.dao.OrderDao;
import com.ewolff.orderhandling.domain.Order;

public class OrderBatchProcess extends AbstractItemStreamItemWriter<Order> {

	private OrderDao orderDao;

	@Autowired
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void write(List<? extends Order> items) throws Exception {
		for (Order item : items) {
			item.setProcessed(true);
			System.out.println("Process "+item);
			orderDao.update(item);
		}
	}

}
