package utils;

import java.util.*;

public class Website {
	private String serviceName;
    private List<Account> accounts;

	public Website(String serviceName) {
		this.serviceName = serviceName;
		// for the accounts, actually we need to read the saved accounts information from the txt file, and I have not finished this.
		this.accounts = new ArrayList<>();
	}

	public String getName() {
        return serviceName;
    }

	public List<Account> getAccounts() {
        return accounts;
    }

	// need to modify the print
	public void printServiceName() {
        System.out.println("Website name is " + serviceName);
    }

	// not finish it
	public boolean deleteAccount(String username) {
        return true;
    }

	// add accounts to the list
	public void addAccount(Account account) {
        accounts.add(account);
    }
	
}

