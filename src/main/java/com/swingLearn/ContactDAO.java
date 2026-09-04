package com.swingLearn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {

	public List<Contact> findAll() throws SQLException {
		String sql = "SELECT e.employee_id, e.first_name, e.last_name, e.phone, e.email, e.department_id, d.department_name AS department_name "
				+ "FROM employees e "
				+ "LEFT JOIN departments d ON e.department_id = d.department_id "
				+ "ORDER BY e.employee_id ASC";

		List<Contact> results = new ArrayList<>();

		try (Connection conn = DatabaseUtil.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				results.add(new Contact(
						rs.getInt("employee_id"),
						rs.getString("first_name"),
						rs.getString("last_name"),
						rs.getString("email"),
						rs.getString("phone"),
						rs.getInt("department_id"),
						rs.getString("department_name")));
			}
		}
		return results;
	}

	public void insert(Contact c) throws SQLException {
		String sql = "INSERT INTO employees (first_name, last_name, phone, email, department_id) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseUtil.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, c.getFname());
			stmt.setString(2, c.getLname());
			stmt.setString(3, c.getPhone());
			stmt.setString(4, c.getEmail());
			stmt.setInt(5, c.getDepartmentId());
			stmt.executeUpdate();
		}
	}

	public void update(Contact c) throws SQLException {
		String sql = "UPDATE employees "
				+ "SET first_name = ?, last_name = ?, phone = ?, email = ?, department_id = ? "
				+ "WHERE employee_id = ?";

		try (Connection conn = DatabaseUtil.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, c.getFname());
			stmt.setString(2, c.getLname());
			stmt.setString(3, c.getPhone());
			stmt.setString(4, c.getEmail());
			stmt.setInt(5, c.getDepartmentId());
			stmt.setInt(6, c.getId());
			stmt.executeUpdate();
		}
	}

	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM employees WHERE employee_id = ?";

		try (Connection conn = DatabaseUtil.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}
}
