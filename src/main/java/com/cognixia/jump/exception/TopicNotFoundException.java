package com.cognixia.jump.exception;

/*
 * 
 * A custom exception for topic not found
 * 
 * Used for methods that involve finding a topic by name or ID
 * 
 */

public class TopicNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public TopicNotFoundException(String message) {
		super(message);
	}
}