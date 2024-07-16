package com.cognixia.jump.exception;

import com.cognixia.jump.dao.Topic;

/*
 * 
 * A custom exception for topic not created
 * 
 * Used for methods that involve creating a new topic
 * 
 */

public class TopicNotCreatedException extends Exception {

	private static final long serialVersionUID = 1L;

	public TopicNotCreatedException(Topic topic) {
		super("Topic with the following values could not be created: " + topic);
	}
	
}

