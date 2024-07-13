package com.cognixia.jump.menu;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.cognixia.jump.dao.User;
import com.cognixia.jump.dao.UserDAO;
import com.cognixia.jump.dao.UserDAOClass;
import com.cognixia.jump.exception.InvalidPasswordException;
import com.cognixia.jump.exception.TopicNotFoundException;
import com.cognixia.jump.exception.TrackerNotCreatedException;
import com.cognixia.jump.exception.UserNotFoundException;
import com.cognixia.jump.dao.Topic;
import com.cognixia.jump.dao.Topic.Category;
import com.cognixia.jump.dao.TopicDAO;
import com.cognixia.jump.dao.TopicDAOClass;
import com.cognixia.jump.dao.Tracker;
import com.cognixia.jump.dao.Tracker.UserStatus;
import com.cognixia.jump.dao.TrackerDAO;
import com.cognixia.jump.dao.TrackerDAOClass;
import com.cognixia.jump.menu.MenuMethods;

/*
 * 
 * The Menu class 
 * 
 * 
 */

public class Menu {

	// scanner to use throughout program.
	private Scanner sc;
	
	// concrete classes to handle CRUD and other operations
    private UserDAO userDAO;
    private TrackerDAO trackerDAO = new TrackerDAOClass();
    private TopicDAO topicDAO = new TopicDAOClass();
    public static User currentUser;
    private MenuMethods menuMethods;

    public Menu(UserDAO userDAO, TopicDAO topicDAO, TrackerDAO trackerDAO) {
        this.userDAO = userDAO;
        this.topicDAO = topicDAO;
        this.trackerDAO = trackerDAO;
        this.sc = new Scanner(System.in);
        this.menuMethods = new MenuMethods(sc, userDAO, trackerDAO, topicDAO, this);
    }
    
    
	public void mainMenu() {

		System.out.println("Progress Tracker");
		System.out.println("------------------");
		
		// used to exit loop
		boolean exit = false;
		
		while(!exit) {
			
			System.out.println("\nPlease Select from the Menu Below");
			System.out.println("1. Login");
			System.out.println("2. Register");
			System.out.println("3. Exit");
			
	        int input = -1;
	        try {
	            input = sc.nextInt();
	            sc.nextLine(); // prevent infinite scanner loop
	        } catch (InputMismatchException e) {
	            System.out.println("\nInvalid input. Please enter a number (1-3).");
	            sc.next(); // clear invalid input
	            continue; // prompt user again
	        }
			
			switch (input) {
			
			case 1:
				menuMethods.login();
				break;
			case 2:
				menuMethods.register();
				break;
			case 3:
				exit = true;
				break;
			default:
				System.out.println("\nPlease enter an option listed (number 1 - 3)");
				break;
			}
			
		}
		
		System.out.println("\n\nexiting");
		
		// close scanner upon exit
		sc.close();
		System.exit(0);
	}	    
    
    public void userMenu() {

		System.out.println("\nProgress Tracker: " + currentUser.getUsername());
		System.out.println("------------------");
		
		// used to exit loop
		boolean exit = false;
		
		while(!exit) {
			
			System.out.println("\nPlease Select from the Menu Below");
			System.out.println("1. My Trackers");
			System.out.println("2. New Tracker");
			System.out.println("3. Tracker Stats");
			System.out.println("4. Account Settings");
			System.out.println("5. Logout");
			System.out.println("6. Exit");
			
	        int input = -1;
	        try {
	            input = sc.nextInt();
	            sc.nextLine(); // prevent infinite scanner loop
	        } catch (InputMismatchException e) {
	            System.out.println("\nInvalid input. Please enter a number (1-6).");
	            sc.next(); // clear invalid input
	            continue; // prompt user again
	        }
			
			switch (input) {
			
			case 1:
				myTrackers();
				break;
			case 2:
				newTrackerMenu();
				break;
			case 3:
				trackerStatsMenu();
				break;
			case 4:
				accountSettingsMenu();
				break;
			case 5:
				mainMenu();
				break;
			case 6:
				exit = true;
				break;
			default:
				System.out.println("\nPlease enter an option listed (number 1 - 6)");
				break;
			}
			
		}
		
		System.out.println("\n\nexiting");
		
		// close scanner upon exit
		sc.close();
		System.exit(0);
	}
    
    public void trackerStatsMenu() {
		// TODO Auto-generated method stub
		
	}

	public void accountSettingsMenu() {
		// TODO Auto-generated method stub
		
	}

	public void adminMenu() {
		System.out.println("\nProgress Tracker: " + currentUser.getUsername());
		System.out.println("------------------");
		
		// used to exit loop
		boolean exit = false;
		
		while(!exit) {
			
			System.out.println("\nPlease Select from the Menu Below");
			System.out.println("1. My Trackers");
			System.out.println("2. New Tracker");
			System.out.println("3. Tracker Stats");
			System.out.println("4. Account Settings");
			System.out.println("5. Logout");
			System.out.println("6. Exit");
			
	        int input = -1;
	        try {
	            input = sc.nextInt();
	            sc.nextLine(); // prevent infinite scanner loop
	        } catch (InputMismatchException e) {
	            System.out.println("\nInvalid input. Please enter a number (1-6).");
	            sc.next(); // clear invalid input
	            continue; // prompt user again
	        }
			
			switch (input) {
			
			case 1:
				myTrackers();
				break;
			case 2:
				newTrackerMenu();
				break;
			case 3:
				trackerStatsMenu();
				break;
			case 4:
				accountSettingsMenu();
				break;
			case 5:
				mainMenu();
				break;
			case 6:
				exit = true;
				break;
			default:
				System.out.println("\nPlease enter an option listed (number 1 - 6)");
				break;
			}
			
		}
		
		System.out.println("\n\nexiting");
		
		// close scanner upon exit
		sc.close();
		System.exit(0);
	}
    
    public void myTrackers() {
    	System.out.println("\nPlease Select from the Menu Below");
		System.out.println("1. View/Update Trackers");
		System.out.println("2. View Favorites");
		System.out.println("3. Go Back");
		
        int input = -1;
        try {
            input = sc.nextInt();
            sc.nextLine(); // prevent infinite scanner loop
        } catch (InputMismatchException e) {
            System.out.println("\nInvalid input. Please enter a number (1-3).");
            sc.next(); // clear invalid input
            //continue; // prompt user again
        }
		
		switch (input) {
		
		case 1:
			viewTrackersCategoryMenu();
			break;
		case 2:
			menuMethods.viewFavorites();
			break;
		case 3:
			userMenu();
			break;

		default:
			System.out.println("\nPlease enter an option listed (number 1 - 3)");
			break;
		}
    	
    }
    
	public void viewTrackersCategoryMenu() {
		
		try {
			if ((trackerDAO.getAllTrackersByUser(Menu.currentUser) != null) && (!trackerDAO.getAllTrackersByUser(Menu.currentUser).isEmpty())) {
				System.out.println("Select a Category:");
				System.out.println("1. TV Shows");
				System.out.println("2. Books");
				System.out.println("3. Music Albums");
				System.out.println("4. Cancel");
				
			    int input = -1;
			    try {
			        input = sc.nextInt();
			        sc.nextLine(); // prevent infinite scanner loop
			    } catch (InputMismatchException e) {
			        System.out.println("\nInvalid input. Please enter a number (1-4).");
			        sc.next(); // clear invalid input
			        //continue; // prompt user again
			    }
				
				switch (input) {
				
				case 1:
					viewTrackersStatusMenu(Category.SERIES);
					break;
				case 2:
					viewTrackersStatusMenu(Category.BOOK);
					break;		
				case 3:
					viewTrackersStatusMenu(Category.ALBUM);
					break;
				case 4:
					userMenu();
					break;
				default:
					System.out.println("\nPlease enter an option listed (number 1 - 4)");
					break;
				}
			}
			
			else {
                System.out.println("No trackers yet. Select New Tracker to track your selection!");
                userMenu();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

    	
		
	}
	
	public void viewTrackersStatusMenu(Category category) {
		// set the specific words to appear appropriately based on category and status
		String action = null;
		String actions = null;
		
        if(category == Category.SERIES) {
        	action = "watch";
        	actions = "watching";
        }
        
        else if(category == Category.BOOK) {
        	action = "read";
        	actions = "reading";
        }
        
        else if(category == Category.ALBUM) {
        	action = "listen";
        	actions = "listening";
        }
        
		System.out.println("Select a Status:");
        System.out.println("1. Plan to " + action);
        System.out.println("2. Currently " + actions);
        System.out.println("3. Finished " + actions);
        System.out.println("4. Cancel");
		
        int input = -1;
        try {
            input = sc.nextInt();
            sc.nextLine(); // prevent infinite scanner loop
        } catch (InputMismatchException e) {
            System.out.println("\nInvalid input. Please enter a number (1-4).");
            sc.next(); // clear invalid input
            //continue; // prompt user again
        }
		
		switch (input) {
		
		case 1:
			menuMethods.viewTrackers(category, UserStatus.NOT_STARTED);
			break;
		case 2:
			menuMethods.viewTrackers(category, UserStatus.IN_PROGRESS);
			break;		
		case 3:
			menuMethods.viewTrackers(category, UserStatus.COMPLETED);
			break;
		case 4:
			userMenu();
			break;
		default:
			System.out.println("\nPlease enter an option listed (number 1 - 4)");
			break;
		}
	}

	public void newTrackerMenu() {
    	System.out.println("Select a Category:");
		System.out.println("1. TV Shows");
		System.out.println("2. Books");
		System.out.println("3. Music Albums");
		System.out.println("4. Cancel");
		
        int input = -1;
        try {
            input = sc.nextInt();
            sc.nextLine(); // prevent infinite scanner loop
        } catch (InputMismatchException e) {
            System.out.println("\nInvalid input. Please enter a number (1-3).");
            sc.next(); // clear invalid input
            //continue; // prompt user again
        }
		
		switch (input) {
		
		case 1:
			menuMethods.newTracker(Category.SERIES, "Series");
			break;
		case 2:
			menuMethods.newTracker(Category.BOOK, "Book");
			break;		
		case 3:
			menuMethods.newTracker(Category.ALBUM, "Album");
			break;
		case 4:
			userMenu();
			break;
		default:
			System.out.println("\nPlease enter an option listed (number 1 - 4)");
			break;
		}
    }



}
