package com.cognixia.jump.dao;

import java.sql.SQLException;
import java.util.List;

/*
 * 
 * The DAO interface for the users entity
 * 
 * Used to establish the CRUD methods needed to make changes 
 * and retrieve data pertaining to users
 * 
 */

public interface UserDAO {
	
	// needed for later so we make sure that the connection manager gets called
	public void establishConnection() throws ClassNotFoundException, SQLException;
	
	// as well, this method will help with closing the connection
	public void closeConnection() throws SQLException ;
	
	// return a list containing all registered users
	public List<User> getAllUsers() throws SQLException;
	
	// return a user given a user ID
	public User getUserByID(int userID) throws SQLException, UserNotFoundException;
	
	// return a user given a username
	public User getUserByUsername(String username) throws SQLException, UserNotFoundException;
	
	// register a new user
	public boolean createUser(User user) throws SQLException;
	
	// delete a user
	public boolean deleteUser(int userID) throws SQLException;
	
	// edit user info
	public boolean updateUser(User user) throws SQLException;
	
	// return true if a user is an admin
	public boolean isAdmin(User user) throws SQLException;
	
}
