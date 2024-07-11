package com.cognixia.jump.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cognixia.jump.exception.TrackerNotCreatedException;
import com.cognixia.jump.exception.TrackerNotFoundException;

/*
 * 
 * The DAO interface for the tracker entity
 * 
 * Used to establish the CRUD methods needed to make changes 
 * and retrieve data pertaining to trackers
 * 
 */

public interface TrackerDAO {
	
	// ensure the connection manager is called
	public void establishConnection() throws ClassNotFoundException, SQLException;
	
	// ensure the connection is closed
	public void closeConnection() throws SQLException;
	
	// return a list of all trackers for a given user
	public List<Tracker> getAllTrackersByUser(User user) throws SQLException;
	
	// return a list of all trackers for a given topic
    public List<Tracker> getAllTrackersByTopic(Topic topic) throws SQLException;
    
    // return a list of a user's favorite topics 
    public List<Topic> showFavorites(User user) throws SQLException;
	
	// return a tracker given a tracker ID
	public Tracker getTrackerByID(int trackerID) throws SQLException, TrackerNotFoundException;
		
	// create a new tracker
	public void createTracker(Tracker tracker) throws SQLException, TrackerNotCreatedException;	
		 
	// delete a tracker
	public boolean deleteTracker(int trackerID) throws SQLException;	
	
	// edit tracker info
	public boolean updateTracker(Tracker tracker) throws SQLException;
	
	// get the average rating for a topic across all users
	public double getAverageRatingForTopic(Topic topic) throws SQLException;
	
	// get the tracker reports for a given topic
	public Map<String, Integer> getTrackerReportForTopic(Topic topic) throws SQLException;

}
