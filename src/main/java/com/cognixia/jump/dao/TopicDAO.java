package com.cognixia.jump.dao;

import java.sql.SQLException;
import java.util.List;

import com.cognixia.jump.exception.TopicNotFoundException;

/*
 * 
 * The DAO interface for the topic entity
 * 
 * Used to establish the CRUD methods needed to make changes 
 * and retrieve data pertaining to topics
 * 
 */

public interface TopicDAO {
	
	// ensure the connection manager is called
	public void establishConnection() throws ClassNotFoundException, SQLException;
	
	// ensure the connection is closed
	public void closeConnection() throws SQLException;
	
	// return a list containing all topics
	public List<Topic> getAllTopics() throws SQLException;
	
	// return a list of all topics in a given category
    public List<Topic> getTopicsByCategory(Topic.Category category) throws SQLException;
	
	// return a topic given a topic ID
	public Topic getTopicByID(int topicID) throws SQLException, TopicNotFoundException;
	
	// return a topic given a topic name
	public Topic getTopicByName(String topicName) throws SQLException, TopicNotFoundException;
	
	// create a new topic
	public boolean createTopic(Topic topic, User admin) throws SQLException;	// pass in admin to restrict privilege
		 
	// delete a topic
	public boolean deleteTopic(int topicID, User admin) throws SQLException;	// pass in admin to restrict privilege
	
	// edit topic info
	public boolean updateTopic(Topic topic, User admin) throws SQLException;	// pass in admin to restrict privilege
	
}
