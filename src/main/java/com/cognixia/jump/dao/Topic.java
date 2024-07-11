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
		SERIES("Series"), 
		BOOK("Book"), 
		ALBUM("Album");
		
        private String value;

        // constructor
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
	private int length;
	private Category category;
	
	// constructor accepting topic attributes as arguments
	public Topic(int topicID, String topicName, int length, Category category) {
		super();
		this.topicID = topicID;
		this.topicName = topicName;
		this.length = length;
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
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
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
        String lengthUnit;
        switch (category) {
            case SERIES:
                lengthUnit = " episodes";
                break;
            case BOOK:
                lengthUnit = " pages";
                break;
            case ALBUM:
                lengthUnit = " songs";
                break;
            default:
                lengthUnit = "";
        }
        return "Topic [topicID=" + topicID + ", topicName=" + topicName + ", length=" + length + lengthUnit + ", category=" + category + "]";
    }
	
}
