package com.swingLearn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
	
	public List<Contact> findAll() throws SQLException {
		String sql = "SELECT * FROM employee ORDER BY id ASC";
		
		List<Contact> results = new ArrayList<>();
		
		try (Connection conn = DatabaseUtil.getConnection(); 
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()){
			while(rs.next()) {
				results.add(new Contact (
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("phone"),
						rs.getString("email")
				));
			}
		} catch (SQLException e) {    		
    		System.out.println("Error: " + e.getMessage());
    	}
		
		return results;
	}
	
	public void insert(Contact c) throws SQLException {
		String sql = "INSERT INTO Employees (first_name, last_name, phone, email, department_id) VALUES (?, ?, ?, ?, ?)";
		
		try (Connection conn = DatabaseUtil.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, c.getName());  
			stmt.setString(2, c.getPhone());
			stmt.setString(3, c.getEmail());
			stmt.executeUpdate();
		} catch (SQLException e) {    		
    		System.out.println("Error: " + e.getMessage());
    	}
	}
	
	public void update(Contact c) throws SQLException {
		String sql = "UPDATE employee SET name = ?, phone = ?, email = ? WHERE id = ?";
		
		try (Connection conn = DatabaseUtil.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)){ 
			stmt.setString(1, c.getName());
			stmt.setString(2, c.getPhone());
			stmt.setString(3, c.getEmail());
			stmt.setInt(4, c.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {    		
    		System.out.println("Error: " + e.getMessage());
    	}
	
	}
	
	public void delete(int id) throws SQLException {
		String sql = "DELETE FROM employee WHERE id = ?";
		
		try (Connection conn = DatabaseUtil.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)){ 
			
			stmt.setInt(1, id);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
