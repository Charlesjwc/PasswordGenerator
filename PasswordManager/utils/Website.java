package utils;

import java.util.*;

/**	Website class for password manager. Holds multiple account objects.
 */
public class Website {
	/*	Field variables	*/
	private String serviceName;
    private List<Account> accounts;

	/*	Constructor	*/
	public Website(String serviceName) {
		this.serviceName = serviceName;
		this.accounts = new ArrayList<>();
	}
	
	/*	Accessor methods	*/
	public String getName() {
        return serviceName;
    }
	public List<Account> getAccounts() {
        return accounts;
    }
	
	/**	Deletes account with given username
	 * 	@param	username of the account to delete
	 * 	@return	true if account deleted, false if account not found
	 */
	public boolean deleteAccount(String username) {
        return accounts.removeIf(account -> account.getUsername().equals(username));
    }

	/**	Adds account object to the list
	 * 	@param	usdernname of the account to add
	 */
	public void addAccount(Account account) {
        accounts.add(account);
    }
	
	// Shouldnt need to print service name, now that we are using swing
	public void printServiceName() {
        System.out.println("Website name is " + serviceName);
    }
}

