package utils;

import java.util.*;

public class User {
	/*	Field variables	*/
	private final String NAME;
	private final String PASSWORD_HASH;
	private List<Account> accounts;
	private List<Website> websites;
	
	/*	Constructors	*/
	public User(String name, String passHash) {
		this.NAME = name;
		this.PASSWORD_HASH = passHash;
		this.websites = new ArrayList<>();
		this.emails = new ArrayList<>();
	}
	
	/**	Accessors	*/
	public String getName() {return NAME;}
	public String getPass() {return PASSWORD_HASH;}
	public List<Website> getAccounts() {return websites;}
	public List<Email> getEmails() {return emails;)
	
	/**	File where this user's information will be stored
	 * 	@return	filepath to files directory, then adds .txt to name
	 */
	public String getFilePath() {
		return "../files/" + NAME + ".txt";
	}
}

