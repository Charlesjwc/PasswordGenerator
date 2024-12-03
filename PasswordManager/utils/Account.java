package utils;

/**	Account class for password manager. Holds information about an account.
 * 	Contains website the accound belongs to, as well as the the username, email, phone, and
 * 	password of account.
 */
public class Account {
	/*	Field variables	*/
	private Website website;
    private String username;
    private String email;
    private String phone;
    private String password;

    private Generator generator = new Generator();
	
	/*	Constructor	
	 * 	Pass in empty stringts if nothing provided for a field
	 */
	public Account(Website website, String username, String email, String phone, String password) {
		this.website = website;
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.password = password;
	}
	
	/**	Set or change the password into a randomly generated one
	 */
	public void setAutoPassword() {
        this.password = generator.autoGenerator();

    }
	/**	Set the password to a manually entered one
	 */
    public void setManualPassword(){
        this.password = generator.manualGenerator();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

	/**	Set/change usernamne
	 * 	@param	newUserName		username to change to
	 */
    public void setNewUserName(String newUserName) {
        this.username = newUserName;
    }
	
	/*	Accessor methods	*/
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public Website getWebsite() {
        return website;
    }
	
	public String toString() {
		return username + " " + email + " " + phone + " " + password;
	}
	
	//	Shouldnt need these anymore because we are using swing
	public void printServiceName() {
        System.out.println("Website: " + website.getName());
    }

    public void printUsername() {
        System.out.println("Username: " + username);
    }

    public void printPassword() {
        System.out.println("Password: " + password);
    }
}

