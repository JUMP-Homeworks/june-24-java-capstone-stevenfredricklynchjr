package com.cognixia.jump.menu;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.cognixia.jump.dao.User;
import com.cognixia.jump.dao.UserDAO;
import com.cognixia.jump.dao.Topic.Category;
import com.cognixia.jump.dao.TopicDAO;
import com.cognixia.jump.dao.Tracker.UserStatus;
import com.cognixia.jump.dao.TrackerDAO;
import com.cognixia.jump.dao.TrackerDAOClass;

/*
 * 
 * The Menu class for establishing the console-based menu
 * 
 * Used to implement functionality of the menu layers, with 
 * more complex methods delegated to MenuMethods class
 * 
 * NOTE FOR TESTING: can initially login with: testuser | testuserpassword
 * 											   testadmin | testadminpassword
 * 
 */

public class Menu {

	// scanner to use throughout program.
	private Scanner sc;
	
	// concrete classes to handle CRUD and other operations
    private TrackerDAO trackerDAO = new TrackerDAOClass();
    public static User currentUser;
    private MenuMethods menuMethods;

    // constructor
    public Menu(UserDAO userDAO, TopicDAO topicDAO, TrackerDAO trackerDAO) {
        this.trackerDAO = trackerDAO;
        this.sc = new Scanner(System.in);
        this.menuMethods = new MenuMethods(sc, userDAO, trackerDAO, topicDAO, this);
    }
    
    // initial menu before login
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
    
	// main menu for users after login
    public void userMenu() {

		System.out.println("\nProgress Tracker: " + currentUser.getUsername());
		System.out.println("------------------");
		
		// used to exit loop
		boolean exit = false;
		
		while(!exit) {
			
			System.out.println("\nPlease Select from the Menu Below");
			System.out.println("1. My Trackers");
			System.out.println("2. New Tracker");
			System.out.println("3. View Tracker Reports");
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
				categoryMenu(1);
				break;
			case 3:
				categoryMenu(2);
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

    // menu for editing user account settings
	public void accountSettingsMenu() {
			System.out.println("\nAccount Settings: Select an Option Below");
			System.out.println("1. Change Username");
			System.out.println("2. Change Password");
			System.out.println("3. Go Back");
			
	        int input = -1;
	        try {
	            input = sc.nextInt();
	            sc.nextLine(); // prevent infinite scanner loop
	        } catch (InputMismatchException e) {
	            System.out.println("\nInvalid input. Please enter a number (1-3).");
	            sc.next(); // clear invalid input	            
	        }
			
			switch (input) {
			
			case 1:
				menuMethods.editUsername();
				break;
			case 2:
				menuMethods.editPassword();
				break;
			case 3:
				
				if(currentUser.getIsAdmin()){
					adminMenu();
				}
				
				userMenu();
				break;
				
			default:
				System.out.println("\nPlease enter an option listed (number 1 - 3)");
				break;
			}
			
		
		
	}

	// main menu for admins after login
	public void adminMenu() {
		System.out.println("\nProgress Tracker ADMIN: " + currentUser.getUsername());
		System.out.println("------------------");
		
		// used to exit loop
		boolean exit = false;
		
		while(!exit) {
			
			System.out.println("\nPlease Select from the Menu Below");
			System.out.println("1. My Trackers");
			System.out.println("2. New Tracker");
			System.out.println("3. View Tracker Reports");
			System.out.println("4. Account Settings");
			System.out.println("5. Admin Settings");
			System.out.println("6. Logout");
			System.out.println("7. Exit");
			
	        int input = -1;
	        try {
	            input = sc.nextInt();
	            sc.nextLine(); // prevent infinite scanner loop
	        } catch (InputMismatchException e) {
	            System.out.println("\nInvalid input. Please enter a number (1-7).");
	            sc.next(); // clear invalid input
	            continue; // prompt user again
	        }
			
			switch (input) {
			
			case 1:
				myTrackers();
				break;
			case 2:
				categoryMenu(1);
				break;
			case 3:
				categoryMenu(2);
				break;
			case 4:
				accountSettingsMenu();
				break;
			case 5:
				adminSettingsMenu();
				break;
			case 6:
				mainMenu();
				break;
			case 7:
				exit = true;
				break;
			default:
				System.out.println("\nPlease enter an option listed (number 1 - 7)");
				break;
			}
			
		}
		
		System.out.println("\n\nexiting");
		
		// close scanner upon exit
		sc.close();
		System.exit(0);
	}
    
	// menu for admin-only actions
    public void adminSettingsMenu() {
    	System.out.println("\nSelect Admin Action");
		System.out.println("1. View Users");
		System.out.println("2. Manage Topics"); // add, delete, update topic info
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
			menuMethods.viewUsers();
			break;
		case 2:
			manageTopicsMenu();
			break;
		case 3:
			adminMenu();
			break;

		default:
			System.out.println("\nPlease enter an option listed (number 1 - 3)");
			break;
		}
		
	}
    
    // menu for admins to update topics
    public void manageTopicsMenu() {
    	System.out.println("\nManage Topics");
		System.out.println("1. Add New Topic");
		System.out.println("2. Update/Delete Topics"); // add, delete, update topic info
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
			menuMethods.newTopic();
			break;
		case 2:
			categoryMenu(3);
			break;
		case 3:
			adminSettingsMenu();
			break;

		default:
			System.out.println("\nPlease enter an option listed (number 1 - 3)");
			break;
		}
    }

    // menu for viewing trackers
	public void myTrackers() {
    	System.out.println("\nPlease Select from the Menu Below");
		System.out.println("1. Manage Trackers");
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
			if(currentUser.getIsAdmin()){
				adminMenu();
			}
			userMenu();
			break;

		default:
			System.out.println("\nPlease enter an option listed (number 1 - 3)");
			break;
		}
    	
    }
    
	// menu for selecting category of tracker to view
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
					if(currentUser.getIsAdmin()){
						adminMenu();
					}
					
					userMenu();
					break;
				default:
					System.out.println("\nPlease enter an option listed (number 1 - 4)");
					break;
				}
			}
			
			else {
                System.out.println("No trackers yet. Select New Tracker to track your selection!");
                if(currentUser.getIsAdmin()){
    				adminMenu();
    			}
                
                userMenu();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

    	
		
	}
	
	// menu for selecting status of trackers to view
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
        
		System.out.println("\nSelect a Status:");
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
			if(currentUser.getIsAdmin()){
				adminMenu();
			}
			
			userMenu();
			break;
		default:
			System.out.println("\nPlease enter an option listed (number 1 - 4)");
			break;
		}
	}

	// category menu for when outcome does not depend on current trackers for user
	public void categoryMenu(int path) { // path allows us to reuse categoryMenu for different purposes
    	System.out.println("\nSelect a Category:");
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
			if(path == 1) {
				menuMethods.newTracker(Category.SERIES, "Series");
			}
			
			if(path == 2) {
				menuMethods.viewTrackerReports(Category.SERIES);
			}
			
			if(path == 3) {
				menuMethods.updateTopics(Category.SERIES);
			}
			
			break;
		case 2:
			if(path == 1) {
				menuMethods.newTracker(Category.BOOK, "Book");
			}
			
			if(path == 2) {
				menuMethods.viewTrackerReports(Category.BOOK);
			}
			
			if(path == 3) {
				menuMethods.updateTopics(Category.BOOK);
			}
			
			break;		
		case 3:
			if(path == 1) {
				menuMethods.newTracker(Category.ALBUM, "Album");
			}
			
			if(path == 2) {
				menuMethods.viewTrackerReports(Category.ALBUM);
			}
			
			if(path == 3) {
				menuMethods.updateTopics(Category.ALBUM);
			}
			
			break;
		case 4:
			if(currentUser.getIsAdmin()){
				adminMenu();
			}
			userMenu();
			break;
		default:
			System.out.println("\nPlease enter an option listed (number 1 - 4)");
			break;
		}
    }

}
