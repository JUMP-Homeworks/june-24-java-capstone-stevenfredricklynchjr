package com.cognixia.jump.exception;

/*
 * 
 * A custom exception for tracker not found
 * 
 * Used for methods that involve finding a tracker by ID
 * 
 */

public class TrackerNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public TrackerNotFoundException(String message) {
		super(message);
	}
}