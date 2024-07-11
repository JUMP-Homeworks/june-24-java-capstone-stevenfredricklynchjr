package com.cognixia.jump.menu;

import java.sql.SQLException;
import com.cognixia.jump.dao.UserDAO;
import com.cognixia.jump.dao.UserDAOClass;

/*
 * 
 * The main class
 * 
 * Used to boot up the project with the initial main menu
 * 
 */

public class Main {
	
	private static UserDAO userDAO = new UserDAOClass();
	
    public static void main(String[] args) {
        try {
            userDAO.establishConnection();
            Menu.mainMenu();
            userDAO.closeConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
	
}
