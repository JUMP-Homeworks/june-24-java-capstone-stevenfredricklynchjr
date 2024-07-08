drop database if exists tracker_db;
create database tracker_db;
use tracker_db;

-- users
CREATE TABLE users (
    user_id INT PRIMARY KEY 
				AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE, -- avoid duplicate usernames
    password_hash VARCHAR(255) NOT NULL, -- store hashed passwords for security
    is_admin BOOLEAN DEFAULT FALSE
);

-- tracker
CREATE TABLE tracker (
    tracker_id INT PRIMARY KEY
				   AUTO_INCREMENT,
    user_status ENUM('not completed', 'in-progress', 'completed') NOT NULL,
    progress INT,
    rating SMALLINT 
		   CHECK (rating BETWEEN 1 AND 5),	-- keep rating between 1 and 5
    favorite BOOLEAN DEFAULT FALSE, -- extra feature for users to mark favorites
    user_id INT,
    CONSTRAINT fk_tracker_users
	  FOREIGN KEY (user_id) 
      REFERENCES users(user_id),
	topic_id INT,
	CONSTRAINT fk_tracker_topic
	  FOREIGN KEY (topic_id) 
      REFERENCES topic(topic_id)
);

-- topic
CREATE TABLE topic (
	topic_id INT PRIMARY KEY
				 AUTO_INCREMENT,
	topic_name VARCHAR(255) NOT NULL,
    category ENUM('TV Show', 'Book', 'Music')
);
