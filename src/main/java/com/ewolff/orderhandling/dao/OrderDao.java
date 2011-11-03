package com.ewolff.orderhandling.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ewolff.orderhandling.domain.Order;
import com.ewolff.orderhandling.domain.OrderItem;

@Repository
public class OrderDao {

	private JdbcTemplate jdbcTemplate;

	public void create(Order order) {
		jdbcTemplate
				.update(
						"INSERT INTO T_ORDER(C_CUSTOMER, C_PROCESSED, C_EXPRESS, C_INVOICED) VALUES(?, ?, ?, ?)",
						order.getCustomer(), order.isProcessed(), order
								.isExpress(), order.isInvoiced());
		order.setId(jdbcTemplate.queryForInt("call identity()"));
		for (OrderItem orderItem : order.getOrderItem()) {
			jdbcTemplate
					.update(
							"INSERT INTO T_ORDERITEM(C_COUNT, C_ITEM, ID_ORDER) VALUES(?,?,?)",
							orderItem.getCount(), orderItem.getItem(), order
									.getId());
			orderItem.setId(jdbcTemplate.queryForInt("call identity()"));
		}
	}

	public List<Order> findAllOrder() {
		List<Order> result = jdbcTemplate
				.query(
						"SELECT * FROM T_ORDER o",
						new OrderRowMapper());
		for (Order order : result) {
			for (OrderItem orderItem : jdbcTemplate.query(
					"SELECT * FROM T_ORDERITEM i WHERE i.ID_ORDER=? ",
					new OrderItemRowMapper(), order.getId())) {
				order.addOrderItem(orderItem);
			}
		}
		return result;
	}

	public Order findById(int id) {
		Order order = jdbcTemplate.queryForObject(
				"SELECT * FROM T_ORDER o WHERE o.id=?",
				new OrderRowMapper(), id);
		for (OrderItem orderItem : jdbcTemplate.query(
				"SELECT * FROM T_ORDERITEM i WHERE i.ID_ORDER=? ",
				new OrderItemRowMapper(), id)) {
			order.addOrderItem(orderItem);
		}

		return order;
	}

	public void update(Order order) {
		jdbcTemplate
				.update(
						"UPDATE T_ORDER SET C_PROCESSED=?, C_EXPRESS=?, C_CUSTOMER=?, C_INVOICED=? WHERE ID=?",
						order.isProcessed(), order.isExpress(), order
								.getCustomer(), order.isInvoiced(), order.getId());

		for (OrderItem orderItem : order.getOrderItem()) {
			jdbcTemplate.update(
					"UPDATE T_ORDERITEM SET C_COUNT=?, C_ITEM=?  WHERE ID=?",
					orderItem.getCount(), orderItem.getItem(), orderItem
							.getId());
		}
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
