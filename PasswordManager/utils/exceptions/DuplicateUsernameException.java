package utils.exceptions;

public class DuplicateUsernameException extends Exception {
	
	public DuplicateUsernameException(String username) {
		super("ERROR: User '" + username + "' already exists");
	}
}
