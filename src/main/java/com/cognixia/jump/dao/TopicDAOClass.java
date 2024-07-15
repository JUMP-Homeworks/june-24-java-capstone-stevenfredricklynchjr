package com.cognixia.jump.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cognixia.jump.connection.ConnectionManager;
import com.cognixia.jump.dao.Topic.Category;
import com.cognixia.jump.exception.TopicNotCreatedException;
import com.cognixia.jump.exception.TopicNotFoundException;

/*
 * 
 * DAO concrete class for topic
 * 
 * Used to implement methods from the TopicDAO interface
 * 
 */

public class TopicDAOClass implements TopicDAO{

	private Connection conn = null;
	

	
	// ensure the connection manager is called
	@Override
	public void establishConnection() throws ClassNotFoundException, SQLException {
		if(conn == null) {
			conn = ConnectionManager.getConnection();
		}
	}
	
	// ensure the connection is closed
	@Override
	public void closeConnection() throws SQLException {
		conn.close();
	}

	// return a list containing all topics	
	@Override
	public List<Topic> getAllTopics() throws SQLException {
		try {
			// set up statement to get all topics
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM topic");
			
			// create the list of topics
			List<Topic> topicList = new ArrayList<Topic>();
			
			while(rs.next()) {
				// iterate through to get column info
				int id = rs.getInt("topic_id");
				String name = rs.getString("topic_name");
				int length = rs.getInt("length");
				Category category = Category.fromString(rs.getString("category"));
				
				// create Topic from data and add to list
				Topic topic = new Topic(id, name, length, category);
				topicList.add(topic);
			}
			
			// return list created from data
			return topicList;
			
		} catch (SQLException e) {
			System.out.println("Failed to retrieve list of topics from the database");
		}
		
		// return null if exception thrown
		return null;
	}
	
	// return a list of all topics in a given category
	@Override
	public List<Topic> getTopicsByCategory(Category category) throws SQLException {
		try {
			// set up prepared statement to get all topics in given category
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM topic WHERE category = ?");
			pstmt.setString(1, category.getValue());
			
			ResultSet rs = pstmt.executeQuery();
			
			// create the list 
			List<Topic> topicList = new ArrayList<Topic>();
		
			while(rs.next()) {
				// iterate through to get column info
				int id = rs.getInt("topic_id");
				String name = rs.getString("topic_name");
				int length = rs.getInt("length");
				Category topicCategory = Category.fromString(rs.getString("category"));
			
				// create Topic from data and add to list
				Topic topic = new Topic(id, name, length, topicCategory);
				topicList.add(topic);
			}
		
			// return list when finished
			return topicList;
		
		} catch (SQLException e) {
			System.out.println("Failed to retrieve list of topics of this category from the database");
		}
	
		// return null if exception thrown
		return null;
	}
	
	// return a topic given a topic ID
	@Override
	public Topic getTopicByID(int topicID) throws SQLException, TopicNotFoundException {
		try {
			// set up prepared statement to get a topic given an ID
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM topic WHERE topic_id = ?");
			pstmt.setInt(1, topicID);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
			
				// gather data for column info and save to Topic
				int id = rs.getInt("topic_id");
				String name = rs.getString("topic_name");
				int length = rs.getInt("length");
				Category category = Category.fromString(rs.getString("category"));
			
				Topic topic = new Topic(id, name, length, category);
				return topic;
				
			}
				
			else {
				throw new TopicNotFoundException("Topic with ID " + topicID + " not found.");
			}
				
		// since we already handle cases for topic found and not found, we rethrow the exception
		} catch(SQLException e) {
			throw e;
		}
	}

	// return a topic given a topic name
	@Override
	public Topic getTopicByName(String topicName) throws SQLException, TopicNotFoundException {
		try {
			// set up prepared statement to get a topic given a topic name
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM topic WHERE topic_name = ?");
			pstmt.setString(1, topicName);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				// gather data for column info and save to Topic
				int id = rs.getInt("topic_id");
				String name = rs.getString("topic_name");
				int length = rs.getInt("length");
				Category category = Category.fromString(rs.getString("category"));
			
				Topic topic = new Topic(id, name, length, category);
				return topic;
				
			}
			
			else {
				throw new TopicNotFoundException("Topic " + topicName + " not found.");
			}
			
		// since we already handle cases for topic found and not found, we rethrow the exception
		} catch(SQLException e) {
			throw e;
		}
	}

	// create a new topic
	@Override
	public void createTopic(Topic topic, User admin) throws SQLException, TopicNotCreatedException {
		// check that user is an admin with privilege to create new topic
		if(!admin.getIsAdmin() == true) {
			throw new SQLException("User not authorized to create topic");
		}
		
		try {
			// set up prepared statement to create a new topic
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO topic (topic_id, topic_name, length, category) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, 0); // topic_id will auto-increment
            pstmt.setString(2, topic.getTopicName());
            pstmt.setInt(3, topic.getLength());
            pstmt.setString(4, topic.getCategory().getValue());
            
			pstmt.executeUpdate();
			
		// throw exception if creation fails
		} catch(SQLException e) {
			throw new TopicNotCreatedException(topic);
		}
	}

	// delete a topic
	@Override
	public boolean deleteTopic(int topicID, User admin) throws SQLException {
		// check that user is an admin with privilege to delete topic
		if(!admin.getIsAdmin() == true) {
			throw new SQLException("User not authorized to delete topic");
		}
		
		try {
			// set up prepared statement to delete a topic given an ID
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM topic WHERE topic_id = ?");
			pstmt.setInt(1, topicID);
			
			int i = pstmt.executeUpdate();
			
			if(i > 0) {
				return true;
			}
			
		} catch(SQLException e) {
			System.out.println("Topic with id = " + topicID + " not found.");
		}
		
		// return false if deletion fails
		return false;
	}

	// edit topic info
	@Override
	public boolean updateTopic(Topic topic, User admin) throws SQLException {
		// check that user is an admin with privilege to edit topic data
		if(!admin.getIsAdmin() == true) {
			throw new SQLException("User not authorized to delete topic");
		}
		
		try {
			// set up prepared statement to update a topic
			PreparedStatement pstmt = conn.prepareStatement("UPDATE topic SET topic_name = ?, length = ?, category = ? WHERE topic_id = ?");
			pstmt.setString(1,  topic.getTopicName());			
			pstmt.setInt(2, topic.getLength());
			pstmt.setString(3,  topic.getCategory().getValue());
			pstmt.setInt(4, topic.getTopicID());
			
			int i = pstmt.executeUpdate();
			
			if(i > 0) {
				return true;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		// return false if update fails
		return false;
	}

}
