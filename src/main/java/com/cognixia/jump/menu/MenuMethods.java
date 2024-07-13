package com.cognixia.jump.menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.cognixia.jump.dao.Topic;
import com.cognixia.jump.dao.TopicDAO;
import com.cognixia.jump.dao.Tracker;
import com.cognixia.jump.dao.TrackerDAO;
import com.cognixia.jump.dao.User;
import com.cognixia.jump.dao.UserDAO;
import com.cognixia.jump.dao.Topic.Category;
import com.cognixia.jump.dao.Tracker.UserStatus;
import com.cognixia.jump.exception.InvalidPasswordException;
import com.cognixia.jump.exception.TopicNotFoundException;
import com.cognixia.jump.exception.TrackerNotCreatedException;
import com.cognixia.jump.exception.UserNotFoundException;

public class MenuMethods {
    private Scanner sc;
    private UserDAO userDAO;
    private TrackerDAO trackerDAO;
    private TopicDAO topicDAO;
    private Menu menu;  // Reference to the Menu class to navigate between menus

    public MenuMethods(Scanner sc, UserDAO userDAO, TrackerDAO trackerDAO, TopicDAO topicDAO, Menu menu) {
        this.sc = sc;
        this.userDAO = userDAO;
        this.trackerDAO = trackerDAO;
        this.topicDAO = topicDAO;
        this.menu = menu;
    }

    public void login() {
        System.out.print("username: ");
        String username = sc.nextLine();
        System.out.print("password: ");
        String password = sc.nextLine();
        
        try { 
        	
			if(userDAO.authenticateUser(username, password)) {
				Menu.currentUser = userDAO.getUserByUsername(username);

				if(userDAO.isAdmin(Menu.currentUser)) {
					menu.adminMenu();
				}
				
				menu.userMenu();
				
			}
			
		} catch (SQLException | UserNotFoundException | InvalidPasswordException e) {
			System.out.println(e.getMessage());				           
		}
    }

	public void register() {
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
				menu.mainMenu();
				break;				           
			}
        }

        try {
        	
            if (userDAO.createUser(username, password)) {
            	Menu.currentUser = userDAO.getUserByUsername(username);   
            	menu.userMenu();
            } 
           
        } catch (SQLException | UserNotFoundException e) {
            e.printStackTrace();
        }
    }
	
	public void viewTrackers(Category category, UserStatus status) {
		String progressUnit = null;
		String categoryName = null;
		String action = null;
		String actions = null;
        
        // set the specific words to appear appropriately based on category
        if(category == Category.SERIES) {
        	progressUnit = "episode(s)";
        	categoryName = "Series";
        	action = "watch";
        	actions = "watching";
        }
        
        else if (category == Category.BOOK) {
        	progressUnit = "page(s)";
        	categoryName = "Books";
        	action = "read";
        	actions = "reading";
        }
        
        else if (category == Category.ALBUM) {
        	progressUnit = "song(s)";
        	categoryName = "Albums";
        	action = "listen to";
        	actions = "listening to";
        }
		
		try {
			List<Tracker> trackers = trackerDAO.getAllTrackersByUser(Menu.currentUser);
			List<Tracker> viewTrackers = new ArrayList<>();
			
			for (Tracker tracker : trackers) {
	            Topic topic = topicDAO.getTopicByID(tracker.getTopicID());
	            if (topic.getCategory() == category && tracker.getStatus() == status) {
	                viewTrackers.add(tracker);
	            }
	        }
	        
	        if (viewTrackers.isEmpty()) {
	            System.out.println("No trackers found for the selected category and status.");
	            menu.viewTrackersStatusMenu(category);
	        } 
	        
	        else {       	
	        	
	        	if(status == UserStatus.NOT_STARTED) {
	        		System.out.println("\nYour Trackers for " + categoryName + " you plan to " + action + " (Select to Make Changes):");	        		
	        	}
	        	
	        	else if (status == UserStatus.IN_PROGRESS) {
	        		System.out.println("\nYour Trackers for " + categoryName + " you are currently " + actions + " (Select to Make Changes):");
	        	}
	        	
	        	else if (status == UserStatus.COMPLETED) {
	        		System.out.println("\nYour Trackers for " + categoryName + " you finished " + actions + " (Select to Make Changes):");
	        	}
	        	
	        	for (int i = 0; i < viewTrackers.size(); i++) {
                    System.out.println((i + 1) + ". " + topicDAO.getTopicByID(viewTrackers.get(i).getTopicID()).getTopicName());
                    System.out.println("\tProgress: " + viewTrackers.get(i).getProgress() + " " + progressUnit + " out of " + topicDAO.getTopicByID(viewTrackers.get(i).getTopicID()).getLength());
                    if(viewTrackers.get(i).getRating() == 0) {
                    	System.out.println("\tYour Rating: N/A");
                    }
                    
                    else {
                    	System.out.println("\tYour Rating: " + viewTrackers.get(i).getRating());
                    }
                    
                    if(trackerDAO.getAverageRatingForTopic(topicDAO.getTopicByID(viewTrackers.get(i).getTopicID())) == 0) {
                    	System.out.println("\tAverage Rating: N/A\n");
                    }
                    
                    else {
                    	System.out.println("\tAverage Rating: " + trackerDAO.getAverageRatingForTopic(topicDAO.getTopicByID(viewTrackers.get(i).getTopicID())) + "\n");
                    }	                    	                    	                    
                }
                
                System.out.println((viewTrackers.size() + 1) + ". Go Back");
                
                int inputTracker = -1;
                try {
                    inputTracker = sc.nextInt();
                    sc.nextLine(); // prevent infinite scanner loop
                } catch (InputMismatchException e) {
                    System.out.println("\nInvalid input. Please enter a number");
                    sc.next(); // clear invalid input
                }
                
                if (inputTracker > 0 && inputTracker <= viewTrackers.size()) {
                    Tracker selectedTracker = viewTrackers.get(inputTracker - 1);
                    updateTracker(selectedTracker);
                    
                }
                
                else if(inputTracker == viewTrackers.size() + 1) {
                	menu.viewTrackersStatusMenu(category);
                }
                
                else {
                	System.out.println("invalid selection - please try again");
                    viewTrackers(category, status);
                }
	        	
	        }
	        		        		
		} catch (SQLException | TopicNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void updateTracker(Tracker tracker) {
	    String progressUnit = null;
	    String action = null;
	    String actions = null;
	    int rating = tracker.getRating();
	    
		Topic topic = null;
		try {
			topic = topicDAO.getTopicByID(tracker.getTopicID());
		} catch (SQLException | TopicNotFoundException e) {
			e.printStackTrace();
			return; // exit method if broken
		}
		
		// set the specific words to appear appropriately based on category
        if(topic.getCategory() == Category.SERIES) {
        	progressUnit = "episode(s)";
        	action = "watch";
        	actions = "watching";
        }
        
        else if (topic.getCategory() == Category.BOOK) {
        	progressUnit = "page(s)";
        	action = "read";
        	actions = "reading";
        }
        
        else if (topic.getCategory() == Category.ALBUM) {
        	progressUnit = "song(s)";
        	action = "listen to";
        	actions = "listening to";
        }
        
        System.out.println("Update tracker for " + topic.getTopicName());
        System.out.println("\nCurrent progress: " + tracker.getProgress() + " " + progressUnit + " out of " + topic.getLength());       
        
        int progress = -1;
        while(!(progress >= tracker.getProgress() && progress <= topic.getLength())){
    		System.out.println("Set your new " + progressUnit + " progress (0 to " + topic.getLength() + ", not less than current progress): ");
    		
            try {
	            progress = sc.nextInt();
	     
	        } catch (InputMismatchException e) {
	            System.out.println("\nInvalid input. Please enter a number");
	            sc.next(); // clear invalid input
	            continue;
	        }
    	}
        
        if (progress >= 0 && progress <= topic.getLength()) {
            tracker.setProgress(progress);
            // status will be updated automatically based on progress
        } 
        
        else {
            System.out.println("Invalid progress value. Please enter a number between current progress and " + topic.getLength());
            updateTracker(tracker); // prompt user again
        }
        
        System.out.println("\nCurrent Rating: " + rating + " stars out of 5 "); 
        
        // ask for optional rating
        int input = -1; 
        // will repeat until user gives a valid response
        while(!(input == 1 || input == 2)) {
        	System.out.println("Update Rating?");
        	System.out.println("1. Yes");
    		System.out.println("2. No");
        	try {
	            input = sc.nextInt();
	            
	        } catch (InputMismatchException e) {
	            System.out.println("\nInvalid input. Please select 1 (Yes) or 2 (No)");
	            sc.next(); // clear invalid input
	            continue; // prompt user again
	        }                      
        }
        
        if(input == 1) {
        	while(!(rating > 0 && rating <=5)) {
        		System.out.println("Enter New Rating (1 to 5): ");
        		try {
        			rating = sc.nextInt();
        			
        		} catch (InputMismatchException e) {
    	            System.out.println("\nInvalid input. Please select 1-5");
    	            sc.next(); // clear invalid input
    	            continue; // prompt user again
    	        }  
        	}
        	
        	tracker.setRating(rating);
        }
        
        if(tracker.isFavorite()) {
        	// ask for adding to favorites
            input = -1; // reset response
            while(!(input == 1 || input == 2)) {
            	System.out.println("\nRemove from Favorites?");
              	System.out.println("1. Yes");
            	System.out.println("2. No");
            	try {
    	            input = sc.nextInt();
    	            
    	        } catch (InputMismatchException e) {
    	        	System.out.println("\nInvalid input. Please select 1 (Yes) or 2 (No)");
    	            sc.next(); // clear invalid input
    	            continue; // prompt user again
    	        }                      
            }
            
            if(input == 1) {
            	tracker.setFavorite(false);
            }           
        }
        
        else {
        	// ask for adding to favorites
            input = -1; // reset response
            while(!(input == 1 || input == 2)) {
            	System.out.println("\nAdd to Favorites?");
              	System.out.println("1. Yes");
            	System.out.println("2. No");
            	try {
    	            input = sc.nextInt();
    	            
    	        } catch (InputMismatchException e) {
    	        	System.out.println("\nInvalid input. Please select 1 (Yes) or 2 (No)");
    	            sc.next(); // clear invalid input
    	            continue; // prompt user again
    	        }                      
            }
            
            if(input == 1) {
            	tracker.setFavorite(true);
            }     
        }
    	
        
        try {
			if (trackerDAO.updateTracker(tracker)) {
			    System.out.println("Tracker updated successfully.");
			} else {
			    System.out.println("Failed to update tracker.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
            
        
        
	}
	
    public void newTracker(Category category, String categoryName) {
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

            int inputTopic = -1;
            try {
                inputTopic = sc.nextInt();
                sc.nextLine(); // prevent infinite scanner loop
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input. Please enter a number");
                sc.next(); // clear invalid input
            }

            if (inputTopic > 0 && inputTopic <= topics.size()) {
                Topic selectedTopic = topics.get(inputTopic - 1);
                
                // check if the user is already tracking topic
                List<Tracker> userTrackers = trackerDAO.getAllTrackersByUser(Menu.currentUser);
                for (Tracker tracker : userTrackers) {
                    if (tracker.getTopicID() == selectedTopic.getTopicID()) {
                        System.out.println("Already tracking this topic. See 'My Trackers' to update.");
                        menu.userMenu();
                    }
                }

                System.out.println("Select the status category:");
                System.out.println("1. Plan to " + action);
                System.out.println("2. Currently " + actions);
                System.out.println("3. Finished " + actions);
                System.out.println("4. Go Back");
                
                int inputStatus = -1;
                try {
                    inputStatus = sc.nextInt();
                    sc.nextLine(); // prevent infinite scanner loop
                } catch (InputMismatchException e) {
                    System.out.println("\nInvalid input. Please enter a number (1-4).");
                    sc.next(); // clear invalid input
                    //continue; // prompt user again
                }

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
                    	while(!(progress > 0 && progress <= selectedTopic.getLength())){
                    		System.out.println("Set your " + progressUnit + " progress (0 to " + selectedTopic.getLength() + "): ");
                            try {
                	            progress = sc.nextInt();
                	     
                	        } catch (InputMismatchException e) {
                	            System.out.println("\nInvalid input. Please enter a number");
                	            sc.next(); // clear invalid input
                	            continue;
                	        }
                    	}
                          
                        //progress = sc.nextInt();
                        //sc.nextLine();
                        
                        // ask for optional rating
                        int input = -1; 
                        // will repeat until user gives a valid response
                        while(!(input == 1 || input == 2)) {
                        	System.out.println("\nGive Rating?");
                        	System.out.println("1. Yes");
                    		System.out.println("2. No");
                        	try {
                	            input = sc.nextInt();
                	            
                	        } catch (InputMismatchException e) {
                	            System.out.println("\nInvalid input. Please select 1 (Yes) or 2 (No)");
                	            sc.next(); // clear invalid input
                	            continue; // prompt user again
                	        }                      
                        }
                        
                        if(input == 1) {
                        	while(!(rating > 0 && rating <=5)) {
                        		System.out.println("Rate the " + categoryName + " (1 to 5): ");
                        		try {
                        			rating = sc.nextInt();
                        			
                        		} catch (InputMismatchException e) {
                    	            System.out.println("\nInvalid input. Please select 1-5");
                    	            sc.next(); // clear invalid input
                    	            continue; // prompt user again
                    	        }  
                        	}
                        }
                        
                    	// ask for adding to favorites
                        input = -1; // reset response
                        while(!(input == 1 || input == 2)) {
                        	System.out.println("\nAdd to Favorites?");
                          	System.out.println("1. Yes");
                        	System.out.println("2. No");
                        	try {
                	            input = sc.nextInt();
                	            
                	        } catch (InputMismatchException e) {
                	        	System.out.println("\nInvalid input. Please select 1 (Yes) or 2 (No)");
                	            sc.next(); // clear invalid input
                	            continue; // prompt user again
                	        }                      
                        }
                        
                        if(input == 1) {
                        	favorite = true;
                        }
                                         
                        sc.nextLine();
                        break;
                        
                    case 3:
                    	selectedStatus = UserStatus.COMPLETED;
                    	while(!(rating > 0 && rating <=5)) {
                    		System.out.println("Rate the " + categoryName + " (1 to 5): ");
                    		try {
                    			rating = sc.nextInt();
                    			
                    		} catch (InputMismatchException e) {
                	            System.out.println("\nInvalid input. Please select 1-5");
                	            sc.next(); // clear invalid input
                	            continue; // prompt user again
                	        }  
                    	}                    	
                    	
                    	// ask for adding to favorites
                        input = -1; // reset response
                        while(!(input == 1 || input == 2)) {
                        	System.out.println("\nAdd to Favorites?");
                          	System.out.println("1. Yes");
                        	System.out.println("2. No");
                        	try {
                	            input = sc.nextInt();
                	            
                	        } catch (InputMismatchException e) {
                	        	System.out.println("\nInvalid input. Please select 1 (Yes) or 2 (No)");
                	            sc.next(); // clear invalid input
                	            continue; // prompt user again
                	        }                      
                        }
                        
                        if(input == 1) {
                        	favorite = true;
                        }
                    	
                    	sc.nextLine();
                        break;
                    case 4:
                    	newTracker(category, categoryName);
                    default:
                        System.out.println("Invalid selection.");
                        newTracker(category, categoryName);
                        return;
                }
                         
          
                Tracker newTracker = new Tracker(0, selectedStatus, progress, rating, favorite, Menu.currentUser.getUserID(), selectedTopic.getTopicID());
                newTracker.setUserID(Menu.currentUser.getUserID());
                newTracker.setTopicID(selectedTopic.getTopicID());
                newTracker.setStatus(selectedStatus);
                newTracker.setProgress(progress);
                newTracker.setRating(rating);
                newTracker.setFavorite(favorite);

                // Call the createTracker method, which will internally call updateProgressAndStatus
                trackerDAO.createTracker(newTracker);

                System.out.println("New tracker created for " + selectedTopic.getTopicName());
            } 
            
            else if(inputTopic == topics.size() + 1) {
            	menu.userMenu();
            }
            
            else {
            	System.out.println("invalid selection - please try again");
                newTracker(category, categoryName);
            }
            
        } catch (SQLException | TrackerNotCreatedException e) {
            e.printStackTrace();
        }
    }
    
    // small extra feature to allow users to view a list of their favorite topics
    public void viewFavorites() {
        try {
            List<Topic> favorites = trackerDAO.showFavorites(Menu.currentUser);
            if (favorites != null && !favorites.isEmpty()) {
                System.out.println("Your favorite Topics:");
                for (Topic topic : favorites) {
                    System.out.println(topic.getTopicName());
                }              
            } 
            
            else {
                System.out.println("No favorites yet. Add or update trackers to mark your favorite topics!");
            }
            
            
            int input = -1;
            while(input != 1) {
            	System.out.println("\n Press 1 to Go Back");
            	try {
    	            input = sc.nextInt();
    	            sc.nextLine(); // prevent infinite scanner loop
    	        } catch (InputMismatchException e) {
    	            System.out.println("\nInvalid input. Press 1 to Go Back.");
    	            sc.next(); // clear invalid input
    	            continue; // prompt user again
    	        }
           
            }
            
            menu.myTrackers();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}