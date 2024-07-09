package com.cognixia.jump.dao;

public class UserNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(User user) {
		super("User not found");
	}
}




	
	
