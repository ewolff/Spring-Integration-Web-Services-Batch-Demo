package com.ewolff.orderhandling.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ewolff.orderhandling.domain.OrderItem;

public class OrderItemRowMapper implements
		RowMapper<OrderItem> {

	public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrderItem orderItem = new OrderItem(rs.getInt("C_COUNT"), rs.getString("C_ITEM"));
		orderItem.setId(rs.getInt("ID"));
		return orderItem;
	}

}
