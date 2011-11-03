package com.ewolff.orderhandling.fullfillment;

import java.util.List;

import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.ewolff.orderhandling.dao.OrderDao;
import com.ewolff.orderhandling.domain.Order;

public class OrderBatchInvoice extends AbstractItemStreamItemWriter<Order> {

	private OrderDao orderDao;

	@Autowired
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void write(List<? extends Order> items) throws Exception {
		for (Order item : items) {
			item.setInvoiced(true);
			System.out.println("Invoice "+item);
			orderDao.update(item);
		}
	}

}
