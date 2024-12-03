package utils.exceptions;

public class UserPassNullException extends Exception {
	
	public UserPassNullException(String username) {
		super("ERROR: Could not read file for user '" + username + 
						"', password not provided");
	}
}
