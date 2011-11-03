package com.ewolff.orderhandling.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {

	private boolean express;
	private int customer;
	private int id;
	private boolean processed = false;
	private boolean invoiced = false;
	private List<OrderItem> orderItem = new ArrayList<OrderItem>();


	public boolean isInvoiced() {
		return invoiced;
	}

	public void setInvoiced(boolean invoiced) {
		this.invoiced = invoiced;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public Order(boolean express, int customer) {
		super();
		this.express = express;
		this.customer = customer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isExpress() {
		return express;
	}

	public void setExpress(boolean express) {
		this.express = express;
	}

	public int getCustomer() {
		return customer;
	}

	public void setCustomer(int customer) {
		this.customer = customer;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Order)) {
			return false;
		}
		Order otherOrder = (Order) other;
		if (otherOrder.customer != customer) {
			return false;
		}
		if (otherOrder.orderItem.size() != orderItem.size()) {
			return false;
		}
		for (OrderItem o : orderItem) {
			boolean found = false;
			for (OrderItem othero : otherOrder.orderItem) {
				found = true;
			}
			if (!found) {
				return false;
			}
		}
		return ((otherOrder.express == express)
				&& (otherOrder.processed == processed)
				&& (otherOrder.invoiced == invoiced) && (otherOrder.customer == customer));

	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("[Order] " + (express ? " express" : "") + " customer"
				+ customer);
		for (OrderItem o : orderItem) {
			result.append(o.toString());
		}
		return result.toString();
	}

	public List<OrderItem> getOrderItem() {
		return orderItem;
	}

	public void addOrderItem(OrderItem orderItem) {
		this.orderItem.add(orderItem);
	}

}
