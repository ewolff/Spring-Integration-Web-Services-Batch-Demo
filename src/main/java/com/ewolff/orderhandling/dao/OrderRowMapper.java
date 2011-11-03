/**
 * 
 */
package com.ewolff.orderhandling.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ewolff.orderhandling.domain.Order;

public class OrderRowMapper implements
		RowMapper<Order> {

	public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
		Order result = new Order(rs.getBoolean("C_EXPRESS"), rs
				.getInt("C_CUSTOMER"));
		result.setId(rs.getInt("ID"));
		result.setProcessed(rs.getBoolean("C_PROCESSED"));
		result.setInvoiced(rs.getBoolean("C_INVOICED"));
		return result;
	}

}