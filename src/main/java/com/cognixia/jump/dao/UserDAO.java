package com.cognixia.jump.dao;

import java.sql.SQLException;
import java.util.List;

import com.cognixia.jump.exception.InvalidPasswordException;
import com.cognixia.jump.exception.UserNotFoundException;

/*
 * 
 * The DAO interface for the users entity
 * 
 * Used to establish the CRUD methods needed to make changes 
 * and retrieve data pertaining to users
 * 
 */

public interface UserDAO {
	
	// ensure the connection manager is called
	public void establishConnection() throws ClassNotFoundException, SQLException;
	
	// ensure the connection is closed
	public void closeConnection() throws SQLException;
	
	// return a list containing all registered users
	public List<User> getAllUsers() throws SQLException;
	
	// return a user given a user ID
	public User getUserByID(int userID) throws SQLException, UserNotFoundException;
	
	// return a user given a username
	public User getUserByUsername(String username) throws SQLException, UserNotFoundException;
	
	// register a new user
	public boolean createUser(String username, String plainTextPassword) throws SQLException;
	
    // authenticate a user with a username and password
    public boolean authenticateUser(String username, String plainTextPassword) throws SQLException, UserNotFoundException, InvalidPasswordException;
	
	// delete a user
	public boolean deleteUser(int userID) throws SQLException;
	
	// edit user info
	public boolean updateUser(User user) throws SQLException;
	
	// create an admin user that is able to add, remove, and edit topic information 
	public boolean createAdmin(String username, String plainTextPassword) throws SQLException;
	
	// return true if a user is an admin
	public boolean isAdmin(User user) throws SQLException;
	
}
