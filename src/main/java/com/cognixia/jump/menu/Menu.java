package com.cognixia.jump.menu;

import java.sql.SQLException;
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

/*
 * 
 * The Menu class 
 * 
 * 
 */

public class Menu {

	// scanner to use throughout program.
	private static Scanner sc;
	
	// concrete classes to handle CRUD and other operations
    private static UserDAO userDAO = new UserDAOClass();
    private static TrackerDAO trackerDAO = new TrackerDAOClass();
    private static TopicDAO topicDAO = new TopicDAOClass();
    private static User currentUser;

	public static void mainMenu() {
		
		// once we enter menu, can initialize scanner
		sc = new Scanner(System.in);

		System.out.println("Progress Tracker");
		System.out.println("------------------");
		
		// used to exit loop
		boolean exit = false;
		
		while(!exit) {
			
			System.out.println("\nPlease Select from the Menu Below");
			System.out.println("1. Login");
			System.out.println("2. Register");
			System.out.println("3. Exit");
			
			int input = sc.nextInt();
			sc.nextLine(); // prevent infinite scanner loop
			
			switch (input) {
			
			case 1:
				login();
				break;
			case 2:
				register();
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
	}
	
    public static void login() {
        System.out.print("username: ");
        String username = sc.nextLine();
        System.out.print("password: ");
        String password = sc.nextLine();
        
        try {
        	
			if(userDAO.authenticateUser(username, password)) {
				currentUser = userDAO.getUserByUsername(username);
				if(userDAO.isAdmin(currentUser)) {
					adminMenu();
				}
				
				userMenu();
			}
			
		} catch (SQLException | UserNotFoundException | InvalidPasswordException e) {
			System.out.println(e.getMessage());
		}
    }

	public static void register() {
        System.out.print("username: ");
        String username = sc.nextLine();
        System.out.print("password: ");
        String password = sc.nextLine();
        System.out.print("confirm password: ");
        String confirmPassword = sc.nextLine();
        
        while(!confirmPassword.equals(password)) {
        	System.out.println("passwords do not match");
        	System.out.println("1. Try Again");
        	System.out.println("2. Cancel");
        	
        	int input = sc.nextInt();
			sc.nextLine(); // prevent infinite scanner loop
			
			switch (input) {
			
			case 1:
				System.out.print("confirm password: ");
				confirmPassword = sc.nextLine();
				break;
			case 2:
				mainMenu();
				break;				           
			}
        }

        try {
        	
            if (userDAO.createUser(username, password)) {
                 userMenu();
            } 
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void userMenu() {

		System.out.println("Progress Tracker: " + currentUser);
		System.out.println("------------------");
		
		// used to exit loop
		boolean exit = false;
		
		while(!exit) {
			
			System.out.println("\nPlease Select from the Menu Below");
			System.out.println("1. My Trackers");
			System.out.println("2. New Tracker");
			System.out.println("3. View Tracker Reports");
			System.out.println("3. Account Settings");
			System.out.println("4. Logout");
			System.out.println("5. Exit");
			
			int input = sc.nextInt();
			sc.nextLine(); // prevent infinite scanner loop
			
			switch (input) {
			
			case 1:
				myTrackers();
				break;
			case 2:
				newTrackerMenu();
				break;
			case 3:
				accountSettings();
				break;
			case 4:
				mainMenu();
				break;
			case 5:
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
	}
    
    public static void adminMenu() {
    	System.out.println("Progress Tracker: " + currentUser);
		System.out.println("------------------");
		
		// used to exit loop
		boolean exit = false;
		
		while(!exit) {
			
			System.out.println("\nPlease Select from the Menu Below");
			System.out.println("1. My Trackers");
			System.out.println("2. New Tracker");
			System.out.println("3. View Tracker Reports");
			System.out.println("3. Account Settings");
			System.out.println("4. Logout");
			System.out.println("5. Exit");
			
			int input = sc.nextInt();
			sc.nextLine(); // prevent infinite scanner loop
			
			switch (input) {
			
			case 1:
				myTrackers();
				break;
			case 2:
				newTrackerMenu();
				break;
			case 3:
				accountSettings();
				break;
			case 4:
				mainMenu();
				break;
			case 5:
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
		
	}
    
    public static void myTrackers() {
    	System.out.println("\nPlease Select from the Menu Below");
		System.out.println("1. View/Update Trackers");
		System.out.println("2. View/Update Favorites");
		System.out.println("3. Logout");
		System.out.println("3. Go Back");
		System.out.println("4. Return to Menu");
    	
    }
    
    public static void newTrackerMenu() {
    	System.out.println("which would you like to make a new tracker for?");
		System.out.println("1. TV Shows");
		System.out.println("2. Books");
		System.out.println("3. Music Albums");
		System.out.println("4. Cancel");
		
		int input = sc.nextInt();
		sc.nextLine(); // prevent infinite scanner loop
		
		switch (input) {
		
		case 1:
			newTracker(Category.SERIES, "Series");
			break;
		case 2:
			newTracker(Category.BOOK, "Book");
			break;		
		case 3:
			newTracker(Category.ALBUM, "Album");
			break;
		case 4:
			userMenu();
			break;
		}
    }
    
    public static void newTracker(Category category, String categoryName) {
        try {
        	// list of trackers under the selected category
            List<Topic> topics = topicDAO.getTopicsByCategory(category);
            String action = null;
            String actions = null;
            String progressUnit = null;
            
            // set the specific words to appear appropriately based on category and status
            if(categoryName.equals("Series")) {
            	action = "watch";
            	actions = "watching";
            	progressUnit = "episode";
            }
            
            else if (categoryName.equals("Book")) {
            	action = "read";
            	actions = "reading";
            	progressUnit = "page";
            }
            
            else if (categoryName.equals("Album")) {
            	action = "listen";
            	actions = "listening";
            	progressUnit = "song";
            }

            System.out.println("Select a " + categoryName + " to track:");
            for (int i = 0; i < topics.size(); i++) {
                System.out.println((i + 1) + ". " + topics.get(i).getTopicName());
            }
            
            System.out.println((topics.size() + 1) + ". Cancel");

            int inputTopic = sc.nextInt();
            sc.nextLine(); // prevent infinite scanner loop

            if (inputTopic > 0 && inputTopic <= topics.size()) {
                Topic selectedTopic = topics.get(inputTopic - 1);

                System.out.println("Select the status category:");
                System.out.println("1. Plan to " + action);
                System.out.println("2. Currently " + actions);
                System.out.println("3. Finished " + actions);

                int inputStatus = sc.nextInt();
                sc.nextLine(); // prevent infinite scanner loop

                UserStatus selectedStatus = null;
                int progress = 0;
                int rating = 0;
                boolean favorite = false;
  
                switch (inputStatus) {
                    case 1:
                        selectedStatus = UserStatus.NOT_STARTED;
                        break;
                        
                    case 2:
                    	selectedStatus = UserStatus.IN_PROGRESS;
                        System.out.println("Set your " + progressUnit + " progress (0 to " + selectedTopic.getLength() + "): ");
                        progress = sc.nextInt();
                        sc.nextLine();
                        
                        // ask for optional rating
                    	String response = null;
                    	while(!(response.equals("y") || response.equals("n"))) {
                    		System.out.println("Give rating? (y/n)");
                    		response = sc.next();
                    	}
                    	
                    	if(response.equalsIgnoreCase("y")) {
                    		while(!(rating > 0 && rating <=5)) {
                    			System.out.println("Rate the " + categoryName + " (1 to 5): ");
                    			rating = sc.nextInt();
                    		}
                    	}
                    	
                    	// ask for adding to favorites
                    	response = null; // reset response
                    	
                    	// will repeat until user gives a valid response
                    	while(!(response.equals("y") || response.equals("n"))) {
                    		System.out.println("Add to Favorites? (y/n)");
                    		response = sc.next();
                    	}
                    	
                    	if(response.equalsIgnoreCase("y")) {
                    		favorite = true;
                    	}
                    
                        sc.nextLine();
                        break;
                        
                    case 3:
                    	selectedStatus = UserStatus.COMPLETED;
                    	while(!(rating > 0 && rating <=5)) {
                			System.out.println("Rate the " + categoryName + " (1 to 5): ");
                			rating = sc.nextInt();
                		}
                    	
                    	response = null; // reset response
                    	
                    	// will repeat until user gives a valid response
                    	while(!(response.equals("y") || response.equals("n"))) {
                    		System.out.println("Add to Favorites? (y/n)");
                    		response = sc.next();
                    	}
                    	
                    	if(response.equalsIgnoreCase("y")) {
                    		favorite = true;
                    	}
                    	sc.nextLine();
                        break;
                    default:
                        System.out.println("Invalid selection. Returning to menu.");
                        userMenu();
                        return;
                }
                         
          
                Tracker newTracker = new Tracker(0, selectedStatus, progress, rating, favorite, currentUser.getUserID(), selectedTopic.getTopicID());
                newTracker.setUserID(currentUser.getUserID());
                newTracker.setTopicID(selectedTopic.getTopicID());
                newTracker.setStatus(selectedStatus);
                newTracker.setProgress(progress);
                newTracker.setRating(rating);
                newTracker.setFavorite(favorite);

                // Call the createTracker method, which will internally call updateProgressAndStatus
                trackerDAO.createTracker(newTracker);

                System.out.println("New tracker created for " + selectedTopic.getTopicName());
            } 
            
            else {
            	System.out.println("invalid selection - please try again");
                newTracker(category, categoryName);
            }
            
        } catch (SQLException | TrackerNotCreatedException e) {
            e.printStackTrace();
        }
    }


}
