package utils;

import java.util.*;

public class User {
	/*	Field variables	*/
	private final String NAME;
	private final int PASSWORD_HASH;
	private List<Website> websites;
	private List<Email> emails;
	
	/*	Constructors	*/
	public User(String name, int passHash) {
		this.NAME = name;
		this.PASSWORD_HASH = passHash;
		this.websites = new ArrayList<>();
		this.emails = new ArrayList<>();
	}
	
	/**	Accessors	*/
	public String getName() {return NAME;}
	public int getPass() {return PASSWORD_HASH;}
	public List<Website> getAccounts() {return websites;}
	public List<Email> getEmails() {return emails;}
	//	ToString returns only name
	public String toString() {return NAME;}
	
	/**	File where this user's information will be stored
	 * 	@return	filepath to files directory, then adds .txt to name
	 */
	public String getFilePath() {
		return "files/" + NAME + ".txt";
	}
	
	
}

