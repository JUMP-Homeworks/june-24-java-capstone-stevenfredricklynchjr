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
import com.cognixia.jump.exception.TopicNotCreatedException;
import com.cognixia.jump.exception.TopicNotFoundException;
import com.cognixia.jump.exception.TrackerNotCreatedException;
import com.cognixia.jump.exception.UserNotFoundException;

/*
 * 
 * The menu methods class for methods passed by the Menu class
 * 
 * Used for separating functionality for neatness
 * 
 */

public class MenuMethods {
    private Scanner sc;
    private UserDAO userDAO;
    private TrackerDAO trackerDAO;
    private TopicDAO topicDAO;
    private Menu menu;  // Reference to the Menu class to navigate between menus

    // constructor
    public MenuMethods(Scanner sc, UserDAO userDAO, TrackerDAO trackerDAO, TopicDAO topicDAO, Menu menu) {
        this.sc = sc;
        this.userDAO = userDAO;
        this.trackerDAO = trackerDAO;
        this.topicDAO = topicDAO;
        this.menu = menu;
    }

    // login method to log in with existing users
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

    // register method to register a new user
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
	
	// method to  users to see tracker reports for a topic as a whole (How many users have completed a show, still watching a show, etc.)
	public void viewTrackerReports(Category category) {
		String categoryName = null;
		String action = null;
		String actions = null;
		double averageRating = 0.0;
        
		if(category == Category.SERIES) {
        	categoryName = "series";
        	action = "watch";
        	actions = "watching";
        }
    	        	
    	else if (category == Category.BOOK) {  		
        	categoryName = "book";
        	action = "read";
        	actions = "reading";     		
    	}
    	
    	else if (category == Category.ALBUM) {
        	categoryName = "music album";
        	action = "listen";
        	actions = "listening";
    	}
    	
    	List<Topic> topics;
		try {
			topics = topicDAO.getTopicsByCategory(category);
			System.out.println("Select a " + categoryName + " to see user Tracker Reports:");
		    
	        for (int i = 0; i < topics.size(); i++) {
	            System.out.println((i + 1) + ". " + topics.get(i).getTopicName());
	        }
	        
	        System.out.println((topics.size() + 1) + ". Go Back");

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
	            List<Tracker> trackers = trackerDAO.getAllTrackersByTopic(selectedTopic);
	            
	            int planning = 0;
	            int current = 0;
	            int complete = 0;
	            for (Tracker tracker : trackers) {
	            	
	            	UserStatus status = tracker.getStatus();
	            
		            if (status == UserStatus.NOT_STARTED) {
		            	planning = planning + 1;  		           
		            }
		            
		            if (status == UserStatus.IN_PROGRESS) {
		            	current = current + 1;  		           
		            }
		            
		            if (status == UserStatus.COMPLETED) {
		            	complete = complete + 1;  		           
		            }
	            }   
		    
	            averageRating = trackerDAO.getAverageRatingForTopic(selectedTopic);
	            System.out.println("\nTracker Report for " + selectedTopic.getTopicName());
	            System.out.println("Want to " + action + ": " + planning + " users");
	            System.out.println("Currently " + actions + ": " + current + " users");
	            System.out.println("Finished " + actions + ": " + complete + " users");
	            if(averageRating > 0) {
	            	System.out.println("Average User Rating: " + averageRating + " / 5");
	            }
	            
	            else {
	            	System.out.println("Average User Rating: N/A (no ratings yet!");
	            }
	            
	            int backInput = -1;
	            while(backInput != 1) {
	            	System.out.println("\n Press 1 to Go Back");
	            	try {
	    	            backInput = sc.nextInt();
	    	            sc.nextLine(); // prevent infinite scanner loop
	    	        } catch (InputMismatchException e) {
	    	            System.out.println("\nInvalid input. Press 1 to Go Back.");
	    	            sc.next(); // clear invalid input
	    	        }          
	            }
	            
	            viewTrackerReports(category);
	        }
	        
	        else if(inputTopic == topics.size() + 1) {
            	menu.categoryMenu(2);
            }
            
            else {
            	System.out.println("invalid selection - please try again");
                viewTrackerReports(category);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }		               
	}
	
	// method to allow users to view and manage their trackers
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
                    
                    // 0 is the default for when a user has not rated the topic
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
	
	// method from account settings to allow users to change their username
	public void editUsername() {
		System.out.println("\nEnter password to make changes: ");
		String plainTextPassword = sc.next();
		try {
			if(userDAO.authenticateUser(Menu.currentUser.getUsername(), plainTextPassword)) {	
				System.out.println("Current Username: " + Menu.currentUser.getUsername());
				System.out.println("Enter new Username: ");
				String newUserName = sc.next();
				Menu.currentUser.setUsername(newUserName);
				userDAO.updateUser(Menu.currentUser);
			}
			
			else {
				int input = -1; 
	            // will repeat until user gives a valid response
	            while(!(input == 1 || input == 2)) {
	            	System.out.println("Incorrect Password");
	            	System.out.println("1. Try Again");
	        		System.out.println("2. Cancel");
	            	try {
	    	            input = sc.nextInt();
	    	            
	    	        } catch (InputMismatchException e) {
	    	            System.out.println("\nInvalid input. Please select 1 or 2");
	    	            sc.next(); // clear invalid input
	    	            continue; // prompt user again
	    	        }                      
	            }
	            
	            if(input == 1) {
	            	editUsername();	            		          
	            }
	            
	            else {
	            	if(Menu.currentUser.getIsAdmin()) {
	            		menu.adminMenu();
	            	}
	            	
	            	menu.userMenu();
	            }
			}
			
		} catch (SQLException | UserNotFoundException | InvalidPasswordException e) {
			e.printStackTrace();
		}
		
		
	}
	
	// method from account settings to allow users to change their password
	public void editPassword() {
		System.out.println("\nEnter Current password to make changes: ");
		String plainTextPassword = sc.next();
		try {
			if(userDAO.authenticateUser(Menu.currentUser.getUsername(), plainTextPassword)) {	
				System.out.println("Enter new Password: ");
				String newPassword = sc.next();
		
				userDAO.updateUser(Menu.currentUser, newPassword);
			}
			
			else {
				int input = -1; 
	            // will repeat until user gives a valid response
	            while(!(input == 1 || input == 2)) {
	            	System.out.println("Incorrect Password");
	            	System.out.println("1. Try Again");
	        		System.out.println("2. Cancel");
	            	try {
	    	            input = sc.nextInt();
	    	            
	    	        } catch (InputMismatchException e) {
	    	            System.out.println("\nInvalid input. Please select 1 or 2");
	    	            sc.next(); // clear invalid input
	    	            continue; // prompt user again
	    	        }                      
	            }
	            
	            if(input == 1) {
	            	editUsername();	            		          
	            }
	            
	            else {
	            	if(Menu.currentUser.getIsAdmin()) {
	            		menu.adminMenu();
	            	}
	            	
	            	menu.userMenu();
	            }
			}
			
		} catch (SQLException | UserNotFoundException | InvalidPasswordException e) {
			e.printStackTrace();
		}
		
		
	}
	
	// method to allow users to make changes to their trackers, especially for updating progress
	public void updateTracker(Tracker tracker) {
	    String progressUnit = null;
	 // set the specific words to appear appropriately based on category
        try {
			if(topicDAO.getTopicByID(tracker.getTopicID()).getCategory() == Category.SERIES) {
				progressUnit = "episode(s)";
			} else
				try {
					if(topicDAO.getTopicByID(tracker.getTopicID()).getCategory() == Category.BOOK) {
						progressUnit = "page(s)";
					}
					
					else if(topicDAO.getTopicByID(tracker.getTopicID()).getCategory() == Category.ALBUM) {
						progressUnit = "song(s)";
					}
				} catch (SQLException | TopicNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} catch (SQLException | TopicNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	    int rating = tracker.getRating();
	    
		Topic topic = null;
		try {
			topic = topicDAO.getTopicByID(tracker.getTopicID());
		} catch (SQLException | TopicNotFoundException e) {
			e.printStackTrace();
			return; // exit method if broken
		}
		
		// ask for action
        int input = -1; 
        // will repeat until user gives a valid response
        while(!(input == 1 || input == 2 || input == 3)) {
        	System.out.println("What would you like to do?");
        	System.out.println("1. Update Tracker");
    		System.out.println("2. Delete Tracker");
    		System.out.println("3. Cancel");
        	try {
	            input = sc.nextInt();
	            
	        } catch (InputMismatchException e) {
	            System.out.println("\nInvalid input. Please select 1 (Update) or 2 (Delete)");
	            sc.next(); // clear invalid input
	            continue; // prompt user again
	        }                      
        }
        
        if(input == 2) {
        	input = -1; 
            // will repeat until user gives a valid response
            while(!(input == 1 || input == 2)) {
            	System.out.println("Are you sure you want to delete this tracker?");
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
            	try {
    				trackerDAO.deleteTracker(tracker.getTrackerID());
    			} catch (SQLException e) {
    				e.printStackTrace();
    			}
            	
            	System.out.println("Tracker Successfully Deleted");
            	try {
					viewTrackers(topicDAO.getTopicByID(tracker.getTopicID()).getCategory(), tracker.getStatus());
				} catch (SQLException | TopicNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            else if (input == 2){
            	try {
					viewTrackers(topicDAO.getTopicByID(tracker.getTopicID()).getCategory(), tracker.getStatus());
				} catch (SQLException | TopicNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        
        else if(input == 3) {
        	try {
				viewTrackers(topicDAO.getTopicByID(tracker.getTopicID()).getCategory(), tracker.getStatus());
			} catch (SQLException | TopicNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
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
        
        if(rating == 0) {            
            // ask for optional rating
            input = -1; 
            // will repeat until user gives a valid response
            while(!(input == 1 || input == 2)) {
            	System.out.println("Give Rating?");
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
            		System.out.println("Enter Rating (1 to 5): ");
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
        }
        
        else {
        	System.out.println("\nCurrent Rating: " + rating + " stars out of 5 "); 
            
            // ask for optional rating
            input = -1; 
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
	
	// method for users to make a new tracker
    public void newTracker(Category category, String categoryName) {
        try {
        	// list of topics under the selected category
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
                        if(Menu.currentUser.getIsAdmin() == true) {
                        	menu.adminMenu();
                        }
                        
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

                // call the createTracker method, which will internally call updateProgressAndStatus
                trackerDAO.createTracker(newTracker);

                System.out.println("New tracker created for " + selectedTopic.getTopicName());
            } 
            
            else if(inputTopic == topics.size() + 1) {
            	if(Menu.currentUser.getIsAdmin() == true) {
                	menu.adminMenu();
                }
            	
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
    
    // admin only method to view users and their info, with passwords protected by hashing
    public void viewUsers() {
    	try {
            List<User> users = userDAO.getAllUsers();
            if (users != null) {
                for (User user : users) {
                    System.out.println(user);
                }
                
                int input = 0;
                System.out.println("\nMake new admin?");
                System.out.println("1. Yes");
                System.out.println("2. No");
            	try {
    	            input = sc.nextInt();
    	           
    	        } catch (InputMismatchException e) {
    	            System.out.println("\nInvalid input");
    	            sc.next(); // clear invalid input
    	        }
            	
            	if(input == 1) {
            		String newAdmin = null;
            		System.out.println("Enter username of user to make new admin");
            		try {
        	            newAdmin = sc.next();
        	           
        	        } catch (InputMismatchException e) {
        	            System.out.println("\nInvalid input");
        	            sc.next(); // clear invalid input
        	        }
            		
            		try {
            			User user = userDAO.getUserByUsername(newAdmin);
            			user.setIsAdmin(true);
						userDAO.updateUser(user);
						System.out.println("successfully added " + newAdmin + " as new admin");
						
					} catch (UserNotFoundException e) {
						e.getMessage();
					}
            	}
            	
            	else {
            		menu.adminMenu();
            	} 
            } 
            
            else {
                System.out.println("No users found or error occurred.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve users.");
            e.printStackTrace();
        }
    }
    
    // admin only method to create new topics
    public void newTopic() {
    	Category category = null;
    	String artist = null;
    	String topicName = null;
    	int length = 0;
    	
    	System.out.println("Select a Category for New Topic:");
		System.out.println("1. TV Show");
		System.out.println("2. Book");
		System.out.println("3. Music Album");
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
			category = Category.SERIES;
			System.out.println("Series Name: ");
			topicName = sc.next();
			
			while(!(length > 0)) {
				System.out.println("Number of Episodes: ");
				input = -1;
		        try {
		            length = sc.nextInt();		            
		        } catch (InputMismatchException e) {
		            System.out.println("\nInvalid input. Please enter a number of episodes");
		            sc.next(); // clear invalid input
		        }        
			}
			
			break;
		case 2:
			category = Category.BOOK;
			System.out.println("Author Name: ");
			artist = sc.next();
			
			System.out.println("Book Name: ");
			topicName = artist + ": " + sc.next();
			
			while(!(length > 0)) {
				System.out.println("Number of Pages: ");
				input = -1;
		        try {
		            length = sc.nextInt();		            
		        } catch (InputMismatchException e) {
		            System.out.println("\nInvalid input. Please enter a number of episodes");
		            sc.next(); // clear invalid input
		        }        
			}
			
			break;		
		case 3:
			category = Category.ALBUM;
			System.out.println("Artist Name: ");
			artist = sc.next();
			
			System.out.println("Album Name: ");
			topicName = artist + ": " + sc.next();
			
			while(!(length > 0)) {
				System.out.println("Number of Songs: ");
				input = -1;
		        try {
		            length = sc.nextInt();		            
		        } catch (InputMismatchException e) {
		            System.out.println("\nInvalid input. Please enter a number of episodes");
		            sc.next(); // clear invalid input
		        }        
			}
			
			break;
		case 4:
			menu.adminMenu();
			break;
		default:
			System.out.println("\nPlease enter an option listed (number 1 - 4)");
			break;
		}
		
		Topic topic = new Topic(0, topicName, length, category);
		
		try {
			topicDAO.createTopic(topic, Menu.currentUser);
		} catch (SQLException | TopicNotCreatedException e) {
			e.printStackTrace();
		}
		
		System.out.println("New Topic Created Successfully");
		
		
    }
    
    // update existing topic info - admin only
    public void updateTopics(Category category) {
    	String lengthUnit = null;
   		if(category == Category.SERIES) {
   			lengthUnit = "episodes";
   		} 
   		
   		if(category == Category.BOOK) {
   			lengthUnit = "pages";
   		}
   					
   		if(category == Category.ALBUM) {
   			lengthUnit = "songs";
   		}
    	
	    List<Topic> topics;
		try {
			topics = topicDAO.getTopicsByCategory(category);
			System.out.println("Select a " + category.getValue() + " to update:");
		    
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
	    		// ask for action
	            int input = -1; 
	            // will repeat until user gives a valid response
	            while(!(input == 1 || input == 2 || input == 3)) {
	            	System.out.println("What would you like to do?");
	            	System.out.println("1. Update Topic");
	        		System.out.println("2. Delete Topic");
	        		System.out.println("3. Cancel");
	            	try {
	    	            input = sc.nextInt();
	    	            
	    	        } catch (InputMismatchException e) {
	    	            System.out.println("\nInvalid input. Please select 1 (Update) or 2 (Delete)");
	    	            sc.next(); // clear invalid input
	    	            continue; // prompt user again
	    	        }                      
	            }
	            
	            if(input == 2) {
	            	input = -1; 
	                // will repeat until user gives a valid response
	                while(!(input == 1 || input == 2)) {
	                	System.out.println("Are you sure you want to delete this topic?");
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
	                	try {
	        				topicDAO.deleteTopic(selectedTopic.getTopicID(), Menu.currentUser);
	        			} catch (SQLException e) {
	        				e.printStackTrace();
	        			}
	                	
	                	System.out.println("Topic Successfully Deleted");
	               
	    					updateTopics(category);
	    				
	                }
	                
	                else if (input == 2){
	               
	                		updateTopics(category);
	    				
	                }
	            }
	            
	            else if(input == 3) {
	        
	            		updateTopics(category);
	    			
	            }
	             
	            System.out.println("Current Name: " + selectedTopic.getTopicName());
	           
	            input = -1; 
	            // will repeat until user gives a valid response
	            while(!(input == 1 || input == 2)) {
	            	System.out.println("\nUpdate " + selectedTopic.getCategory().getValue() + " name?");
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
	            	System.out.println("Updated Name: ");
					selectedTopic.setTopicName(sc.next());
	            	System.out.println(selectedTopic.getCategory().getValue() + " Successfully Renamed");
	            }
	            
	            
	            System.out.println("\nCurrent Length: " + selectedTopic.getLength() + " " + lengthUnit);
	            input = -1; 
	            // will repeat until user gives a valid response
	            while(!(input == 1 || input == 2)) {
	            	System.out.println("\nUpdate " + selectedTopic.getCategory().getValue() + " length?");
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
	            	
	            	int newLength = -1;
	            	boolean validInput = false;
	            	while(!validInput) {
	            		System.out.println("Updated Length: ");
	            		try {
	                		newLength = sc.nextInt();
	                		validInput = true;
	                	} catch (InputMismatchException e) {
	        	            System.out.println("\nInvalid input. Enter a number");
	        	            sc.next(); // clear invalid input
	        	        }                 	
	            	}
	            	
	            	selectedTopic.setLength(newLength);
	            	System.out.println("New Length Set Successfully");
	            }
	            
	            System.out.println("\nCurrent Category: " + selectedTopic.getCategory().getValue());
	            input = -1; 
	            // will repeat until user gives a valid response
	            while(!(input == 1 || input == 2)) {
	            	System.out.println("\nUpdate topic category?");
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
	        		
	            	input = -1; // reset input
	            	
	            	while(!(input == 1 || input == 2 || input == 3)) {
	                	System.out.println("Select New Category (or choose current to cancel update):");
	                	System.out.println("1. TV Show");
	            		System.out.println("2. Book");
	            		System.out.println("2. Music Album");
	                	try {
	        	            input = sc.nextInt();
	        	            if(input == 1) {
	        	            	selectedTopic.setCategory(Category.SERIES);
	        	            }	
	        	            
	        	            else if(input == 2) {
	        	            	selectedTopic.setCategory(Category.BOOK);
	        	            }
	        	            
	        	            else if(input == 3) {
	        	            	selectedTopic.setCategory(Category.ALBUM);
	        	            }
	        	            
	        	        } catch (InputMismatchException e) {
	        	            System.out.println("\nInvalid input. Please select 1 for Series, 2 for Book, or 3 for Album");
	        	            sc.next(); // clear invalid input
	        	            continue; // prompt user again
	        	        }                      
	                }
	            }
	            
	           topicDAO.updateTopic(selectedTopic, Menu.currentUser);
	           System.out.println("Topic Successfully Updated"); 
	           
	           updateTopics(category);
	        }                    
	        
	        else if(inputTopic == topics.size() + 1) {
	        	menu.adminMenu();
	        }
	        
	        else {
	        	System.out.println("invalid selection - please try again");
	            updateTopics(category);
	        }  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
                  
	}

}