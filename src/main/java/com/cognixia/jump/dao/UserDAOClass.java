package com.cognixia.jump.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cognixia.jump.connection.ConnectionManager;
import com.cognixia.jump.exception.InvalidPasswordException;
import com.cognixia.jump.exception.UserNotFoundException;
import com.cognixia.jump.util.PasswordUtil;

/*
 * 
 * DAO concrete class for users
 * 
 * Used to implement methods from the UserDAO interface
 * 
 */
public class UserDAOClass implements UserDAO{
	
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

	// return a list containing all registered users
	@Override
	public List<User> getAllUsers() throws SQLException {
		try {
			// set up statement to get all users
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users");
			
			// create the list of users
			List<User> userList = new ArrayList<User>();
			
			while(rs.next()) {
				// iterate through to get column info
				int id = rs.getInt("user_id");
				String username = rs.getString("username");
				String password = rs.getString("password_hash");
				boolean admin = rs.getBoolean("is_admin");
				
				// create User from data and add to list
				User user = new User(id, username, password, admin);
				userList.add(user);
			}
			
			// return list created from data
			return userList;
			
		} catch (SQLException e) {
			System.out.println("Failed to retrieve list of users from the database");
		}
		
		// return null if exception thrown
		return null;
	}

	// return a user given a user ID
	@Override
	public User getUserByID(int userID) throws SQLException, UserNotFoundException {
		try {
			// set up prepared statement to get a user given an ID
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE user_id = ?");
			pstmt.setInt(1, userID);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
			
				// gather data for column info and save to User
				int id = rs.getInt("user_id");
				String username = rs.getString("username");
				String password = rs.getString("password_hash");
				boolean admin = rs.getBoolean("is_admin");
			
				User user = new User(id, username, password, admin);
				return user;
				
			}
				
			else {
				throw new UserNotFoundException("User with ID " + userID + " not found.");
			}
				
		// since we already handle cases for user found and not found, we rethrow the exception
		} catch(SQLException e) {
			throw e;
		}
	}

	// return a user given a username
	@Override
	public User getUserByUsername(String username) throws SQLException, UserNotFoundException {
		try {
			// set up prepared statement to get a user given a username
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
			pstmt.setString(1, username);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				// gather data for column info and save to User
				int id = rs.getInt("user_id");
				String userName = rs.getString("username");
				String password = rs.getString("password_hash");
				boolean admin = rs.getBoolean("is_admin");
			
				User user = new User(id, userName, password, admin);
				return user;
				
			}
			
			else {
				throw new UserNotFoundException("User " + username + " not found.");
			}
			
		// since we already handle cases for user found and not found, we rethrow the exception
		} catch(SQLException e) {
			throw e;
		}
	}

	// register a new user
	@Override
	public boolean createUser(String username, String plainTextPassword) throws SQLException {
		String hashedPassword = PasswordUtil.hashPassword(plainTextPassword);
		try {
			// check if username already exists
			getUserByUsername(username);
			
			// if no exception is thrown, username already exists
			System.out.println("Account already exists with this username");
			
			// return false if user could not be created
			return false;
			
		} catch(UserNotFoundException e) {
			// set up prepared statement to create a new user given a username and password
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (username, password_hash) VALUES (?, ?)");
			pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            
			int i = pstmt.executeUpdate();
			
			if(i > 0) {
				return true;
			}
			
		// since we already handle cases where user can and cannot be created, we rethrow the exception
		} catch(SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		// return false outside of try/catch
		return false;
	}

    // authenticate a user with a username and password
	@Override
	public boolean authenticateUser(String username, String plainTextPassword) throws SQLException, UserNotFoundException, InvalidPasswordException {
		try {
			// set up prepared statement to authenticate a user given a username and password
			PreparedStatement pstmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?");
			pstmt.setString(1, username);
			
			ResultSet rs = pstmt.executeQuery();
			
			// check if password is correct for username given
			if(rs.next()) {
				String hashedPassword = rs.getString("password_hash");
				if(PasswordUtil.checkPassword(plainTextPassword, hashedPassword)) {
					return true;
				}
				
				// throw InvalidPasswordException if password does not match
				else {
					throw new InvalidPasswordException("Incorrect Password");
				}
			}
			
			// throw UserNotFoundException if username does not exist
			else {
				throw new UserNotFoundException("User " + username + " not found.");
			}	
			
		} catch(SQLException  e) {
			e.printStackTrace();
			return false;
		}
	}

	// delete a user
	@Override
	public boolean deleteUser(int userID) throws SQLException {
		try {
			// set up prepared statement to delete a user given an ID
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE user_id = ?");
			pstmt.setInt(1, userID);
			
			int i = pstmt.executeUpdate();
			
			if(i > 0) {
				return true;
			}
			
		} catch(SQLException e) {
			System.out.println("User with id = " + userID + " not found.");
		}
		
		// return false if deletion fails
		return false;
	}

	// edit user info
	@Override
	public boolean updateUser(User user) throws SQLException {
		try {
			// set up prepared statement to update a user's info
			PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET username = ?, password_hash = ?, is_admin = ?, WHERE user_id = ?");
			pstmt.setString(1,  user.getUsername());
			pstmt.setString(2,  user.getPasswordHash());
			pstmt.setBoolean(3,  user.getIsAdmin());
			pstmt.setInt(4, user.getUserID());
			
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
	
	// create an admin user that is able to add, remove, and edit topic information 
	@Override
	public boolean createAdmin(String username, String plainTextPassword) throws SQLException {
		String hashedPassword = PasswordUtil.hashPassword(plainTextPassword);
		try {
			// check if username already exists
			getUserByUsername(username);
			
			// if no exception is thrown, username already exists
			System.out.println("Account already exists with this username");
			
			// return false if user could not be created
			return false;
			
		} catch(UserNotFoundException e) {
			// set up prepared statement to create a new admin given a username and password
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (username, password_hash, is_admin) VALUES (?, ?, ?)");
			pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setBoolean(3, true);
            
			int i = pstmt.executeUpdate();
			
			if(i > 0) {
				return true;
			}
			
		// since we already handle cases where user can and cannot be created, we rethrow the exception
		} catch(SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		// return false outside of try/catch
		return false;
	}

	// return true if a user is an admin
	@Override
	public boolean isAdmin(User user) throws SQLException {
		try {
			// set up prepared statement to determine if user is an admin
			PreparedStatement pstmt = conn.prepareStatement("SELECT is_admin FROM users WHERE user_id = ?");
			pstmt.setInt(1, user.getUserID());
			
			ResultSet rs = pstmt.executeQuery();
			
			// collect the result to return
			if(rs.next()) {
				return rs.getBoolean("is_admin");
			}
				
		} catch(SQLException e) {
			System.out.println("User with id = " + user.getUserID() + " not found.");
		}
		
		// return false if exception throws (invalid user ID)
		return false;
	}
	
}
