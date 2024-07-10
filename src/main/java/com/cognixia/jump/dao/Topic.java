package com.cognixia.jump.dao;

/*
 * 
 * The DAO model class for the topic entity
 * 
 * Used to establish the constructor to accept attributes, 
 * the getters/setters to manipulate data, and the toString 
 * to return formatted data
 * 
 */

public class Topic {
	
	// define the category enum and match database values with constant to avoid compromising on format
	public enum Category{
		TV_SHOW("TV Show"), 
		BOOK("Book"), 
		MUSIC("Music");
		
        private String value;

        Category(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Category fromString(String text) {
            for (Category c : Category.values()) {
                if (c.value.equalsIgnoreCase(text)) {
                    return c;
                }
            }
            
            throw new IllegalArgumentException("No enum constant " + text);
        }
    }

	// attributes of the topic entity
	private int topicID;
	private String topicName;
	private Category category;
	
	// constructor accepting topic attributes as arguments
	public Topic(int topicID, String topicName, Category category) {
		super();
		this.topicID = topicID;
		this.topicName = topicName;
		this.category = category;
	}

	// getter and setter methods to manipulate attribute data
	public int getTopicID() {
		return topicID;
	}

	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	// toString method to generate a formatted output of topic data
	@Override
	public String toString() {
		return "Topic [topicID=" + topicID + ", topicName=" + topicName + ", category=" + category + "]";
	}
	
	
	
}
