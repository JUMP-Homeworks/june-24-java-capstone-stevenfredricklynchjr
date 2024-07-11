package com.cognixia.jump.dao;

/*
 * 
 * The DAO model class for the tracker entity
 * 
 * Used to establish the constructor to accept attributes, 
 * the getters/setters to manipulate data, and the toString 
 * to return formatted data
 * 
 */

public class Tracker {
	// define the user_status enum and match database values with constant to avoid compromising on format
	public enum UserStatus{
		NOT_STARTED("not started"), 
		IN_PROGRESS("in-progress"), 
		COMPLETED("completed");
		
        private String value;

        // constructor
        UserStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static UserStatus fromString(String text) {
            for (UserStatus s : UserStatus.values()) {
                if (s.value.equalsIgnoreCase(text)) {
                    return s;
                }
            }
            
            throw new IllegalArgumentException("No enum constant " + text);
        }
    }
	
	// attributes of the tracker entity
	private int trackerID;
	private UserStatus status;
	private int progress;
	private int rating;
	private boolean favorite;
	private User user;
	private Topic topic;
	
	// constructor accepting tracker attributes as arguments
	public Tracker(int trackerID, UserStatus status, int progress, int rating, boolean favorite, User user, Topic topic) {
		super();
		this.trackerID = trackerID;
		this.status = status;
		this.progress = progress;
		this.rating = rating;
		this.favorite = favorite;
		this.user = user;
		this.topic = topic;
		
		updateStatusBasedOnProgress(); // ensure the status is set correctly based on progress progress
	}
	
	// getter and setter methods to manipulate attribute data
	public int getTrackerID() {
		return trackerID;
	}
	
	public void setTrackerID(int trackerID) {
		this.trackerID = trackerID;
	}
	
	public UserStatus getStatus() {
		return status;
	}
	
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
		updateStatusBasedOnProgress(); // update status automatically whenever progress is set
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public boolean isFavorite() {
		return favorite;
	}
	
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Topic getTopic() {
		return topic;
	}
	
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	
    // update the status based on the progress
    private void updateStatusBasedOnProgress() {
        if (progress == 0) {
            this.status = UserStatus.NOT_STARTED;
        } 
        
        else if (progress > 0 && progress < topic.getLength()) {
            this.status = UserStatus.IN_PROGRESS;
        } 
        
        else if (progress == topic.getLength() || progress > topic.getLength()) {
            this.status = UserStatus.COMPLETED;
            progress = topic.getLength();
        }
    }


	// toString method to generate a formatted output of tracker data
	@Override
	public String toString() {
		return "Tracker [trackerID=" + trackerID + ", status=" + status + ", progress=" + progress + ", rating="
				+ rating + ", favorite=" + favorite + ", user=" + user + ", topic=" + topic + "]";
	}

}
