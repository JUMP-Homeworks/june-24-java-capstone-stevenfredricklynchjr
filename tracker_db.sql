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

-- topic
CREATE TABLE topic (
	topic_id INT PRIMARY KEY
				 AUTO_INCREMENT,
	topic_name VARCHAR(255) NOT NULL,
    length INT, -- represents episodes for shows, pages for books, songs for music albums
    category ENUM('Series', 'Book', 'Album')
);

-- tracker
CREATE TABLE tracker (
    tracker_id INT PRIMARY KEY
				   AUTO_INCREMENT,
    user_status ENUM('not started', 'in-progress', 'completed') NOT NULL,
    progress INT,
    rating SMALLINT 
		   CHECK (rating BETWEEN 0 AND 5),	-- keep rating between 1 and 5, if 0 then have java functionality to hide
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

INSERT INTO users (username, password_hash, is_admin) VALUES ('testadmin', 'testadminpassword', TRUE);
INSERT INTO users (username, password_hash, is_admin) VALUES ('testuser', 'testuserpassword', FALSE);

INSERT INTO topic (topic_name, length, category) VALUES ('Breaking Bad', 62, 'Series');
INSERT INTO topic (topic_name, length, category) VALUES ('The Sopranos', 86, 'Series');
INSERT INTO topic (topic_name, length, category) VALUES ('The Wire', 60, 'Series');
INSERT INTO topic (topic_name, length, category) VALUES ('Planet Earth', 11, 'Series');
INSERT INTO topic (topic_name, length, category) VALUES ('Band of Brothers', 10, 'Series');
INSERT INTO topic (topic_name, length, category) VALUES ('Game of Thrones', 74, 'Series');
INSERT INTO topic (topic_name, length, category) VALUES ('Batman: The Animated Series', 85, 'Series');
INSERT INTO topic (topic_name, length, category) VALUES ('The Office', 188, 'Series');
INSERT INTO topic (topic_name, length, category) VALUES ('Seinfeld', 173, 'Series');
INSERT INTO topic (topic_name, length, category) VALUES ('Fargo', 51, 'Series');
INSERT INTO topic (topic_name, length, category) VALUES ('Dante: The Divine Comedy', 752, 'Book');
INSERT INTO topic (topic_name, length, category) VALUES ('Albert Camus: The Stranger', 123, 'Book');
INSERT INTO topic (topic_name, length, category) VALUES ('Cormac McCarthy: Blood Meridian', 368, 'Book');
INSERT INTO topic (topic_name, length, category) VALUES ('Fyodor Dostoevsky: Crime and Punishment', 527, 'Book');
INSERT INTO topic (topic_name, length, category) VALUES ('Anthony Burgess: A Clockwork Orange', 192, 'Book');
INSERT INTO topic (topic_name, length, category) VALUES ('Dale Carnegie: How to Win Friends and Influence People', 291, 'Book');
INSERT INTO topic (topic_name, length, category) VALUES ('Marcus Aurelius: Meditations', 146, 'Book');
INSERT INTO topic (topic_name, length, category) VALUES ('Aldous Huxley: Brave New World', 311, 'Book');
INSERT INTO topic (topic_name, length, category) VALUES ('St. Augustine: Confessions', 156, 'Book');
INSERT INTO topic (topic_name, length, category) VALUES ('Charles Bukowski: Ham on Rye', 288, 'Book');
INSERT INTO topic (topic_name, length, category) VALUES ('Death Grips: No Love Deep Web', 13, 'Album');
INSERT INTO topic (topic_name, length, category) VALUES ('John Maus: We Must Become the Pitiless Censors of Ourselves', 11, 'Album');
INSERT INTO topic (topic_name, length, category) VALUES ('Neutral Milk Hotel: In the Aeroplane Over the Sea', 11, 'Album');
INSERT INTO topic (topic_name, length, category) VALUES ('Ariel Pink: Before Today', 12, 'Album');
INSERT INTO topic (topic_name, length, category) VALUES ('Lil Ugly Mane: Mista Thug Isolation', 18, 'Album');
INSERT INTO topic (topic_name, length, category) VALUES ('Bob Dylan: Highway 61 Revisited', 9, 'Album');
INSERT INTO topic (topic_name, length, category) VALUES ('Grateful Dead: American Beauty', 10, 'Album');
INSERT INTO topic (topic_name, length, category) VALUES ('The Band: Music from Big Pink', 11, 'Album');
INSERT INTO topic (topic_name, length, category) VALUES ('The Doors: The Doors', 11, 'Album');
INSERT INTO topic (topic_name, length, category) VALUES ('Madvillain: Madvillainy', 22, 'Album');

INSERT INTO tracker(user_status, progress, rating, favorite, user_id, topic_id) VALUES ('in-progress', 15, 5, TRUE, 1, 9);
INSERT INTO tracker(user_status, progress, rating, favorite, user_id, topic_id) VALUES ('in-progress', 15, 5, TRUE, 1, 20);
INSERT INTO tracker(user_status, progress, rating, favorite, user_id, topic_id) VALUES ('in-progress', 10, 5, TRUE, 1, 22);

INSERT INTO tracker(user_status, progress, rating, favorite, user_id, topic_id) VALUES ('not started', 0, NULL, FALSE, 2, 9);
INSERT INTO tracker(user_status, progress, rating, favorite, user_id, topic_id) VALUES ('in-progress', 55, NULL, FALSE, 2, 20);
INSERT INTO tracker(user_status, progress, rating, favorite, user_id, topic_id) VALUES ('completed', NULL, 5, TRUE, 2, 22);