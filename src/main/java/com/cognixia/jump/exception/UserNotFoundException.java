package com.cognixia.jump.exception;

/*
 * 
 * A custom exception for user not found
 * 
 * Used for methods that involve finding a user, such as 
 * authentication when logging in
 * 
 */

public class UserNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String message) {
		super(message);
	}
}