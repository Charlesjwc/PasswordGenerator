package utils;

import java.util.*;

public class Website {
	private String serviceName;
    private List<Account> accounts;

	public Website(String serviceName) {
		this.serviceName = serviceName;
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


	public boolean deleteAccount(String username) {
        return accounts.removeIf(account -> account.getUsername().equals(username));
    }

	// add accounts to the list
	public void addAccount(Account account) {
        accounts.add(account);
    }
	
}

