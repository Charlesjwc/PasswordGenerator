package exceptions;

public class MissingPasswordException extends FileFormatException {
	
	public MissingPasswordException(String username, String siteName) {
		super("ERROR: Password not found for user '" + username
					+ "' on account for " + siteName + "'");
	}
}
