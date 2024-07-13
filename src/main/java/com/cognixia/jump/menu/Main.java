package com.cognixia.jump.menu;

import java.sql.SQLException;
import com.cognixia.jump.dao.UserDAO;
import com.cognixia.jump.dao.UserDAOClass;
import com.cognixia.jump.dao.TopicDAO;
import com.cognixia.jump.dao.TopicDAOClass;
import com.cognixia.jump.dao.TrackerDAO;
import com.cognixia.jump.dao.TrackerDAOClass;

/*
 * 
 * The main class
 * 
 * Used to boot up the project with the initial main menu
 * 
 */

public class Main {
	
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAOClass();
        TopicDAO topicDAO = new TopicDAOClass();
        TrackerDAO trackerDAO = new TrackerDAOClass();
        try {
            userDAO.establishConnection();
            topicDAO.establishConnection();
            trackerDAO.establishConnection();
            
        } catch (ClassNotFoundException | SQLException e) {
        	System.out.println("\nCould not connect to the Tracker Database, application cannot run at this time.");
        	return;
        }
        
        Menu menu = new Menu(userDAO, topicDAO, trackerDAO);
        menu.mainMenu();
        
        try {
            userDAO.closeConnection();
            topicDAO.closeConnection();
            trackerDAO.closeConnection();
            
		} catch (SQLException e) {
			System.out.println("Could not close connection properly");
		}
    }
	
}
