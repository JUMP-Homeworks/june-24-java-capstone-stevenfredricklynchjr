package com.cognixia.jump.exception;

import com.cognixia.jump.dao.Tracker;

/*
 * 
 * A custom exception for tracker not created
 * 
 * Used for methods that involve creating a new tracker
 * 
 */

public class TrackerNotCreatedException extends Exception {

	private static final long serialVersionUID = 1L;

	public TrackerNotCreatedException(Tracker tracker) {
		super("Tracker with the following values could not be created: " + tracker);
	}
	
}

