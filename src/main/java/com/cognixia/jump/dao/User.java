package com.cognixia.jump.dao;

/*
 * 
 * The DAO model class for the users entity
 * 
 * Used to establish the constructor to accept attributes, 
 * the getters/setters to manipulate data, and the toString 
 * to return formatted data
 * 
 */

public class User {
	
	// attributes of the users entity
	private int userID;
	private String username;
	private String passwordHash;
	private boolean isAdmin;
	
	// constructor accepting users attributes as arguments
	public User(int userID, String username, String passwordHash, boolean isAdmin) {
		super();
		this.userID = userID;
		this.username = username;
		this.passwordHash = passwordHash;
		this.isAdmin = isAdmin;
	}

	// getter and setter methods to manipulate attribute data
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	// toString method to generate a formatted output of user data
	@Override
	public String toString() {
		return "User [userID=" + userID + ", username=" + username + ", passwordHash=" + passwordHash + ", isAdmin="
				+ isAdmin + "]";
	}
}
