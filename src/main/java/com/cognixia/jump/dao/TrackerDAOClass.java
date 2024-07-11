package com.cognixia.jump.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cognixia.jump.connection.ConnectionManager;
import com.cognixia.jump.dao.Topic.Category;
import com.cognixia.jump.dao.Tracker.UserStatus;
import com.cognixia.jump.exception.TrackerNotCreatedException;
import com.cognixia.jump.exception.TrackerNotFoundException;

/*
 * 
 * DAO concrete class for tracker
 * 
 * Used to implement methods from the TrackerDAO interface
 * 
 */

public class TrackerDAOClass implements TrackerDAO {
	
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
	
	// get the tracker reports for a given topic
	@Override
    public Map<String, Integer> getTrackerReportForTopic(Topic topic) {
		
		// hashmap to store the statuses along with each respective count across all users
		Map<String, Integer> report = new HashMap<>();

        try {
        	// set up prepared statement for finding tracker reports across users given a topic
        	PreparedStatement pstmt = conn.prepareStatement("SELECT SUM(CASE WHEN user_status = 'not started' THEN 1 ELSE 0 END) AS not_started_count, "
                    											 + "SUM(CASE WHEN user_status = 'in-progress' THEN 1 ELSE 0 END) AS in_progress_count, "
											                     + "SUM(CASE WHEN user_status = 'completed' THEN 1 ELSE 0 END) AS completed_count "
											                     + "FROM tracker WHERE topic_id = ?");
            pstmt.setInt(1, topic.getTopicID());
            ResultSet rs = pstmt.executeQuery();

            // gather data to populate hashmap
            if (rs.next()) {
                int notStartedCount = rs.getInt("not_started_count");
                int inProgressCount = rs.getInt("in_progress_count");
                int completedCount = rs.getInt("completed_count");

                // place the data
                report.put("not_started_count", notStartedCount);
                report.put("in_progress_count", inProgressCount);
                report.put("completed_count", completedCount);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // return the map with the number of users at each status for a given topic
        return report;
    }

	// return a list of all trackers for a given user
	@Override
	public List<Tracker> getAllTrackersByUser(User user) throws SQLException {
		try {
			// set up prepared statement to get all trackers for a given user
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tracker WHERE user_id = ?");
			pstmt.setInt(1, user.getUserID());
			
			ResultSet rs = pstmt.executeQuery();
			
			// create the list 
			List<Tracker> trackerList = new ArrayList<Tracker>();
		
			while(rs.next()) {
				// iterate through to get column info
				int id = rs.getInt("tracker_id");
				UserStatus userStatus = UserStatus.fromString(rs.getString("user_status"));
				int progress = rs.getInt("progress");
				int rating = rs.getInt("rating");
				boolean favorite = rs.getBoolean("favorite");
				int user_id = rs.getInt("user_id");
				int topic_id = rs.getInt("topic_id");
			
				// create Tracker from data and add to list
				Tracker tracker = new Tracker(id, userStatus, progress, rating, favorite, user_id, topic_id);
				trackerList.add(tracker);
			}
			
			// return list when finished
			return trackerList;
		
		} catch (SQLException e) {
			System.out.println("Failed to retrieve list of trackers for this user");
		}
	
		// return null if exception thrown
		return null;
	}

	// return a list of all trackers for a given topic
	@Override
	public List<Tracker> getAllTrackersByTopic(Topic topic) throws SQLException {
		try {
			// set up prepared statement to get all trackers for a given user
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tracker WHERE topic_id = ?");
			pstmt.setInt(1, topic.getTopicID());
			
			ResultSet rs = pstmt.executeQuery();
			
			// create the list 
			List<Tracker> trackerList = new ArrayList<Tracker>();
		
			while(rs.next()) {
				// iterate through to get column info
				int id = rs.getInt("tracker_id");
				UserStatus userStatus = UserStatus.fromString(rs.getString("user_status"));
				int progress = rs.getInt("progress");
				int rating = rs.getInt("rating");
				boolean favorite = rs.getBoolean("favorite");
				int user_id = rs.getInt("user_id");
				int topic_id = rs.getInt("topic_id");
			
				// create Tracker from data and add to list
				Tracker tracker = new Tracker(id, userStatus, progress, rating, favorite, user_id, topic_id);
				trackerList.add(tracker);
			}
			
			// return list when finished
			return trackerList;
		
		} catch (SQLException e) {
			System.out.println("Failed to retrieve list of trackers for this topic");
		}
	
		// return null if exception thrown
		return null;
	}

    // return a list of a user's favorite topics 
	@Override
	public List<Topic> showFavorites(User user) throws SQLException {
		try {
			// set up prepared statement to get all topics marked favorite by a user
			PreparedStatement pstmt = conn.prepareStatement("SELECT t.topic_id, t.topic_name, t.length, t.category FROM tracker tr "
					   									  + "JOIN topic t "
					   									  + "ON tr.topic_id = t.topic_id "
					   									  + "WHERE tr.user_id = ? AND tr.favorite = TRUE");
			ResultSet rs = pstmt.executeQuery();
			
			// create the list of favorites
			List<Topic> favoriteList = new ArrayList<Topic>();
			
			while(rs.next()) {
				// iterate through to get column info
				int id = rs.getInt("topic_id");
				String name = rs.getString("topic_name");
				int length = rs.getInt("length");
				Category category = Category.fromString(rs.getString("category"));
				
				// create Topic from data and add to list
				Topic topic = new Topic(id, name, length, category);
				
				// add to list of favorites to return
                favoriteList.add(topic);
			}
			
			// return list created from data
			return favoriteList;
			
		} catch (SQLException e) {
			System.out.println("Failed to retrieve list of favorites for the user");
		}
		
		// return null if exception thrown
		return null;
	}

	// return a tracker given a tracker ID
	@Override
	public Tracker getTrackerByID(int trackerID) throws SQLException, TrackerNotFoundException {
		try {
			// set up prepared statement to get a tracker given an ID
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tracker WHERE tracker_id = ?");
			pstmt.setInt(1, trackerID);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
			
				// gather data for column info and save to Tracker
				int id = rs.getInt("tracker_id");
				UserStatus userStatus = UserStatus.fromString(rs.getString("user_status"));
				int progress = rs.getInt("progress");
				int rating = rs.getInt("rating");
				boolean favorite = rs.getBoolean("favorite");
				int user_id = rs.getInt("user_id");
				int topic_id = rs.getInt("topic_id");
			
				Tracker tracker = new Tracker(id, userStatus, progress, rating, favorite, user_id, topic_id);
				return tracker;
				
			}
				
			else {
				throw new TrackerNotFoundException("Tracker with ID " + trackerID + " not found.");
			}
				
		// since we already handle cases for tracker found and not found, we rethrow the exception
		} catch(SQLException e) {
			throw e;
		}
	}

	// create a new tracker
	@Override
	public void createTracker(Tracker tracker) throws SQLException, TrackerNotCreatedException {
		try {
			updateProgressAndStatus(tracker); // automatically prevent inconsistent status/progress values
			
			// set up prepared statement to create a new tracker
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tracker (user_status, progress, rating, favorite, user_id, topic_id) VALUES (?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, tracker.getStatus().getValue());
            pstmt.setInt(2, tracker.getProgress());
            pstmt.setInt(3, tracker.getRating());
            pstmt.setBoolean(4, tracker.isFavorite());
            pstmt.setInt(5, tracker.getUserID());
            pstmt.setInt(6, tracker.getTopicID());
            
            pstmt.executeUpdate();
            
		} catch(SQLException e) {
			throw new TrackerNotCreatedException(tracker); // throw exception if creation fails
		}
		
	}

	// delete a tracker
	@Override
	public boolean deleteTracker(int trackerID) throws SQLException {
		try {
			// set up prepared statement to delete a tracker given an ID
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM tracker WHERE tracker_id = ?");
			pstmt.setInt(1, trackerID);
			
			int i = pstmt.executeUpdate();
			
			if(i > 0) {
				return true;
			}
			
		} catch(SQLException e) {
			System.out.println("Tracker with id = " + trackerID + " not found.");
		}
		
		// return false if deletion fails
		return false;
	}

	// edit tracker info
	@Override
	public boolean updateTracker(Tracker tracker) throws SQLException {
		try {
			updateProgressAndStatus(tracker); // automatically prevent inconsistent status/progress values
			
			// set up prepared statement to update a tracker
			PreparedStatement pstmt = conn.prepareStatement("UPDATE tracker SET user_status = ?, progress = ?, rating = ?, favorite = ? WHERE tracker_id = ?");
            pstmt.setString(1, tracker.getStatus().getValue());
            pstmt.setInt(2, tracker.getProgress());
            pstmt.setInt(3, tracker.getRating());
            pstmt.setBoolean(4, tracker.isFavorite());
            pstmt.setInt(5, tracker.getTrackerID());
			
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
	
	// get the average rating for a topic across all users
	@Override
	public double getAverageRatingForTopic(Topic topic) throws SQLException {
		double averageRating = 0.0; // set up double to hold average value
		try {
			// set up prepared statement to find the average rating across all users for given topic
			PreparedStatement pstmt = conn.prepareStatement("SELECT AVG(rating) FROM tracker WHERE topic_id = ? AND rating IS NOT NULL");
			
			pstmt.setInt(1, topic.getTopicID());
			ResultSet rs = pstmt.executeQuery();
			
			// assign result to the double to return
			if(rs.next()) {
				averageRating = rs.getDouble(1);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return averageRating;
	}
	
	// helper method to maintain data consistency between the user_status and tracker progress
	private void updateProgressAndStatus(Tracker tracker) throws SQLException {

	    try {
	    	// set up prepared statement to get the length of the topic 
	    	PreparedStatement pstmt = conn.prepareStatement("SELECT length FROM topic WHERE topic_id = ?");
	        
	    	pstmt.setInt(1, tracker.getTopicID());
	        ResultSet rs = pstmt.executeQuery();
	        
	        
	        if (rs.next()) {
	            int topicLength = rs.getInt("length");
	            
	            // automatically update progress based on status
	            switch (tracker.getStatus()) {
	                case NOT_STARTED:
	                    tracker.setProgress(0);
	                    break;
	                case IN_PROGRESS:
	                    // progress will be set manually by user in this case
	                    break;
	                case COMPLETED:
	                    tracker.setProgress(topicLength);
	                    break;
	            }
	            
	            // automatically update status based on progress
	            if (tracker.getProgress() == 0) {
	                tracker.setStatus(Tracker.UserStatus.NOT_STARTED);
	            } else if (tracker.getProgress() > 0 && tracker.getProgress() < topicLength) {
	                tracker.setStatus(Tracker.UserStatus.IN_PROGRESS);
	            } else if (tracker.getProgress() >= topicLength) {
	                tracker.setStatus(Tracker.UserStatus.COMPLETED);
	                tracker.setProgress(topicLength); // if user sets progress larger than topic length, automatically set to topic length
	            }
	        }
	        
	    } catch(SQLException e) {
	    	e.printStackTrace();
	    }
	}

}
