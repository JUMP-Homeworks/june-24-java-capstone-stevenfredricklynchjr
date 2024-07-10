package com.cognixia.jump.dao;
import java.sql.SQLException;

import com.cognixia.jump.dao.Topic;
import com.cognixia.jump.dao.Topic.Category;
import com.cognixia.jump.dao.TopicDAO;
import com.cognixia.jump.exception.TopicNotFoundException;
public class Main {

	public static void main(String[] args) throws SQLException, TopicNotFoundException {
		TopicDAO topic = new TopicDAOClass();
		try {
			topic.establishConnection();
			
			
		} catch (ClassNotFoundException | SQLException e1) {
			
			System.out.println("\nCould not connect to the Chef Database, application cannot run at this time.");
		}
		

		
		System.out.println(topic.getTopicsByCategory(Category.fromString("TV Show")));


			try {
				topic.closeConnection();
			} catch (SQLException e) {
				System.out.println("Could not close connection properly");
			}
		}


	
	}


