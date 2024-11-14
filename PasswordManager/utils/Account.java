package utils;

public class Account {
	private Website website;
    private String username;
    private String email;
    private String phone;
    private String password;

    private Generator generator = new Generator();

	public Account(Website website, String username, String email, String phone, String password) {
		this.website = website;
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.password = password;
	}
	
	public void printServiceName() {
        System.out.println("Website: " + website.getName());
    }

    public void printUsername() {
        System.out.println("Username: " + username);
    }

    public void printPassword() {
        System.out.println("Password: " + password);
    }

	public void setAutoPassword() {
        this.password = generator.autoGenerator();

    }

    public void setManualPassword(){
        this.password = generator.manualGenerator();
    }

    public void setNewUserName(String newUserName) {
        this.username = newUserName;
    }

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
		
}

