package com.cognixia.jump.exception;

/*
 * 
 * A custom exception for invalid password
 * 
 * Used for methods that involve user authentication
 * 
 */

public class InvalidPasswordException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
    public InvalidPasswordException(String message) {
        super(message);
    }
}
