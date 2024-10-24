package utils;

import java.util.*;

public class Website {
	/*	Field variable	*/
	private final String NAME;
	private List<Account> accounts;
	
	/*	Constructors	*/
	public Website(String name) {
		this.NAME = name;
		this.accounts = new ArrayList<>();
	}
}

