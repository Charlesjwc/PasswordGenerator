package utils;

import exceptions.*;
import java.util.*;

/**	User class for password manager. By default only has username and password.
 * 	unhashedPassword is blank until user enters a password that matches the
 * 	hashed password and logs in.
 * 	Keeps track of Websites and accounts
 */
public class User {
	/*	Field variables	*/
	private String username;
    private int password; 
    private String unhashedPassword;
    private List<Website> websites;

	private static Set<String> usernames = new HashSet<>(); // track all the usernames
	private static List<User> allUsers = new ArrayList<>(); // track all the users

	private Generator generator = new Generator();

	/*	Constructor	*/
	public User(String username, int password, String unhashedPassword) throws DuplicateUsernameException {
		if (usernames.contains(username)) {
            throw new DuplicateUsernameException(username);
        }

		this.username = username;
		this.password = password;
		this.unhashedPassword = unhashedPassword;
		this.websites = new ArrayList<>(); 

		usernames.add(username);
		allUsers.add(this);
	}

    public User(String username, String unhashedPassword) throws DuplicateUsernameException {
		if (usernames.contains(username)) {
            throw new DuplicateUsernameException(username);
        }

		this.username = username;
		this.unhashedPassword = unhashedPassword;
        this.password = unhashedPassword.hashCode();
		this.websites = new ArrayList<>(); 

		usernames.add(username);
		allUsers.add(this);
	}
	
	/**	Gets the filepath where this user's info will be stored	*/
	public String getFilePath() {
        return "files/" + username + ".txt";
    }
	
	/*	Accessor methods	*/
	public String getUsername() {
        return username;
    }
    public String getPassword() {
        return unhashedPassword;
    }
    public List<Website> getWebsites() {
        return websites;
    }
    public int getHashedPassword() {
        return password;
    }

	/**	Checks whether the enteredPassword is the right password.
	 * 	Hashes entered string, and may provide false positives if two 
	 * 	Strings hash to the same value
	 * 	Sets password if password might be valid
	 * 	@param	enteredPassword to check
	 * 	@return true if password matches hashed, false otherwise
	 */
	public boolean isPassword(String enteredPassword) {
        boolean result = Integer.toString(enteredPassword.hashCode()).equals(Integer.toString(password));
        if (result)
			this.unhashedPassword = enteredPassword;
		return result;
    }
	
	/**	Removes a website from the list
	 * 	@param	serviceName of website to remove
	 * 	@return true if Website deleted, false otherwise
	 */
	public boolean deleteWebsite(String serviceName) {
		return websites.removeIf(website -> website.getName().equals(serviceName));
    }

	/**	Adds a website to list
	 * 	@param	website to add
	 */
	public void addWebsite(Website website) {
        websites.add(website);
    }
	
	/**	Deletes a user from the list
	 * 	@param	username of user to delete
	 * 	@return	true if user deleted, false otherwise
	 */
	public static boolean deleteUser(String username) {
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                allUsers.remove(user); 
                usernames.remove(username); 
                return true; 
            }
        }
        return false; 
    }

	/**	Change password
	 * 	@param	newPassword to change to
	 */
    public boolean changePassword(String newPassword) {
        if (!generator.isValidPassword(newPassword)) {
            System.out.println("Password may contain chars which don't allowed.");
            return false;
        }
        this.unhashedPassword = newPassword;
        this.password = newPassword.hashCode();
        System.out.println("User password successfully changed.");
        return true;
    }



}

