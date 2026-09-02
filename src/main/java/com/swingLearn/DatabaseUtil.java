package com.swingLearn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String USER = "empapp";
    private static final String PASS= "EmpApp123";
    
    public static Connection getConnection() throws SQLException {
    	return DriverManager.getConnection(URL, USER, PASS);
    }
    
}
