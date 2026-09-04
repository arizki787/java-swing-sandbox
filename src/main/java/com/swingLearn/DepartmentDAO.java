package com.swingLearn;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;

public class DepartmentDAO {

	public List<Department> findAll() throws SQLException {
		String sql = "SELECT department_id, department_name FROM departments ORDER BY department_name ASC";
		List<Department> results = new ArrayList<>();

		try (Connection conn = DatabaseUtil.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				results.add(new Department(rs.getInt("department_id"), rs.getString("department_name")));
			}
		}
		return results;
	}
	
	public DefaultCategoryDataset getDeptSalary() throws SQLException {
		String sql = "select d.department_id, d.department_name, sum(e.salary) as total_salary "
					+ "from employees e "
					+ "join departments d on e.department_id = d.department_id "
					+ "group by d.department_id, d.department_name";
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		try (Connection conn = DatabaseUtil.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()){
			
			while(rs.next()) {
				String dept_name = rs.getString("department_name");
				int dept_salary = rs.getInt("total_salary");
				dataset.addValue(dept_salary, "Dept_Salary", dept_name);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dataset;
		
	}
}
