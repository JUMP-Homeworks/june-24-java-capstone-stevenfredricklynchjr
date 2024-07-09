package com.cognixia.jump.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * 
 * The connection manager class 
 * 
 * Used to connect the Java code to MySQL through JDBC
 * 
 */

public class ConnectionManager {
	
	private static final String URL = "jdbc:mysql://localhost:3306/chef_db?serverTimezone=EST5EDT";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "Root@123"; 
	
	private static Connection connection = null;
	

	private static void makeConnection() throws ClassNotFoundException, SQLException {

		Class.forName("com.mysql.cj.jdbc.Driver");
		
		connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

	}

	public static Connection getConnection() throws ClassNotFoundException, SQLException {

		if (connection == null) {
			makeConnection();
		}

		return connection;
	}

	// main method is used to make sure the connection is working as expected
	public static void main(String[] args) {

		Connection conn = null;
		
		try {
			conn = ConnectionManager.getConnection();
			System.out.println("Connected");
			
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			conn.close();
			System.out.println("Closed connection");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
