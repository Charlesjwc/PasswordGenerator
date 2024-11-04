package utils;

import java.util.*;

public class User {
	private String username;
    private int password; 
    private String unhashedPassword;
    private List<Website> websites;
	
	public User(String username, int password, String unhashedPassword) {
		this.username = username;
		this.password = password;
		this.unhashedPassword = unhashedPassword;
		// for the websites, actually we need to read the saved websites information from the txt file, and I have not finished this.
		this.websites = new ArrayList<>(); 
	}
	
	public String getFilePath() {
        return "./users/" + username + ".txt"; // Not defined, wait others' work
    }
	
	public String getUsername() {
        return username;
    }

	// just do the basic hash check now
	public boolean isPassword(String enteredPassword) {
        return Integer.toString(enteredPassword.hashCode()).equals(Integer.toString(password));
    }

	public List<Website> getWebsites() {
        return websites;
    }

	// not finish it
	public boolean deleteWebsite(String serviceName) {
		return true;
    }

	public String getPassword() {
        return unhashedPassword;
    }

	// add websites to the list
	public void addWebsite(Website website) {
        websites.add(website);
    }
	
}

