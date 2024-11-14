package utils;

import java.util.*;

public class User {
	private String username;
    private int password; 
    private String unhashedPassword;
    private List<Website> websites;

	private static Set<String> usernames = new HashSet<>(); // track all the usernames
	
	public User(String username, int password, String unhashedPassword) throws Exception {
		if (usernames.contains(username)) {
            throw new Exception("Username already exists. Please use a different name.");
        }

		this.username = username;
		this.password = password;
		this.unhashedPassword = unhashedPassword;
		this.websites = new ArrayList<>(); 

		usernames.add(username);
	}
	
	public String getFilePath() {
        return "files/" + username + ".txt"; // Not defined, wait others' work
    }
	
	public String getUsername() {
        return username;
    }

	// just do the basic hash check now, and set password to entered password if correct
	public boolean isPassword(String enteredPassword) {
        boolean result = Integer.toString(enteredPassword.hashCode()).equals(Integer.toString(password));
        if (result)
			this.unhashedPassword = enteredPassword;
		return result;
    }

	public List<Website> getWebsites() {
        return websites;
    }

	
	public boolean deleteWebsite(String serviceName) {
		return websites.removeIf(website -> website.getName().equals(serviceName));
    }

	public String getPassword() {
        return unhashedPassword;
    }
    
    //	For writing to file
    public int getHashedPassword() {
        return password;
    }

	// add websites to the list
	public void addWebsite(Website website) {
        websites.add(website);
    }

	public static void removeUsername(String username) {
        usernames.remove(username);
    }
	
}

