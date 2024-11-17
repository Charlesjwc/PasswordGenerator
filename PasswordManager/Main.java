//	Import utils package including User, Website, Password, etc.
import utils.*;

import java.util.*;

/**
 * 	Main.java
 * 	Main class for PasswordManager project
 *
 * 	@author	Charles Chang
 * 	@author	Christine Ryan
 * 	@author	Lei Hao
 * 	@author Yujia Yang
 *
 * 	@since 23 October 2024
 */

public class Main {
	//	List of users
	private List<User> users;
	private Scanner scanner;

	//	Constructor
	public Main() {
		users = new ArrayList<>();
		scanner = new Scanner(System.in);
	}

	public static void main (String[] args) throws Exception {
		//	Create and run instance
		Main m = new Main();
		m.run();
	}

	/**	Runs the program	*/
	private void run() throws Exception {
		//	Initialize fileutil and read users
		System.out.println("Initializing FileUtil and reading existing users.");
		FileUtil.init();
		FileUtil.fileUtil.readUsers(users);

		for (User u: users)
			FileUtil.fileUtil.readUserInfo(u);
		System.out.println();

		// manually test
		User currentUser = userLoginOrCreate();
		System.out.println("Reading user info from file now.");
		FileUtil.fileUtil.readUserInfo(currentUser);

		boolean running = true;
		while (running) {
			System.out.println("1. Vies Websites and Accounts");
			System.out.println("2. Add a Websites and Accounts");
			System.out.println("3. Delete a Websites");
			System.out.println("4. Save and Exit");

			int choice = getIntInput("Enter your choice");
			switch (choice) {
				case 1:
					viewWebsiteAndAccount(currentUser);
					break;
				case 2:
					addWebsiteAndAccount(currentUser);
					break;
				case 3:
					deleteWebsite(currentUser);
					break;
				case 4:
					saveAllUsers();
					break;
				default:
					System.out.println("Invalid choice. Try again.");
			}
		}

		// auto test, no need to enter the username and password.
		System.out.println("Adding test users.");
		addTestUsers();

		System.out.println("\n========== USERS ===========");
		for (User u: users) {
			System.out.println("User: " + u.getUsername());
			for (Website website : u.getWebsites()) {
				System.out.println(" Website: " + website.getName());
				for (Account account : website.getAccounts()) {
					System.out.println("  Account: " + account.toString());
				}
			}
		}

		System.out.println("\n========PASSWORD GENERATOR TEST ==========");
		testPasswordGenerator();

		System.out.println("\nSaving all users.");
		saveAllUsers();
	}

//	private void save() {
//		FileUtil.fileUtil.writeUsers(users);
//	}
//
//	private void saveUser(User u) {
//		FileUtil.fileUtil.writeUserInfo(u);
//	}

	private int getIntInput(String s) {
		System.out.print(s);
		while (!scanner.hasNextInt()) {
			System.out.println("Invalid input. Try again.");
			scanner.next();
		}
		return scanner.nextInt();
	}

	private String getStringInput(String s) {
		System.out.print(s);
		return scanner.next();
	}

	private User userLoginOrCreate() throws Exception {
		System.out.println("Login or create a new user:");
		System.out.println("1. Login to the existing user");
		System.out.println("2. Create a new user");

		int choice = getIntInput("Enter your choice");
		switch (choice) {
			case 1:
				return loginUser();
			case 2:
				return createUser();
			default:
				System.out.println("Invalid choice. Try again.");
				return userLoginOrCreate();
		}
	}

	private User loginUser() {
		System.out.println("Login: ");
		String username = getStringInput("Username: ");
		String password = getStringInput("Password: ");

		for (User user: users) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return user;
			}
		}
		System.out.println("Invalid username. Try again.");
		return loginUser();
	}

	private User createUser() {
		Generator generator = new Generator();

		System.out.println("Create a new user:");
		String username = getStringInput("Username: ");
		System.out.println("Password: ");
		System.out.println("Do you want to manually input a password, or auto generate one?");
		System.out.println("1. manually inpt a password");
		System.out.println("2. auto generate one");

		String password;
		int choice = getIntInput("Enter your choice");
		switch (choice) {
			case 1:
				password = generator.manualGenerator();
				break;
			case 2:
				password = generator.autoGenerator();
				break;
		}
		User newUser = new User(username, password.hashCode(), password);  // todo, the what should be passed in for password.
		users.add(newUser);
		System.out.println("New user created.");
		return newUser;
	}

	private void viewWebsiteAndAccount(User user) {
		System.out.println("Websites and Accounts: ");
		for (Website website : user.getWebsites()) {
			System.out.println("Website: " + website.getName());
			for (Account account : website.getAccounts()) {
				System.out.println("  Account: " + account.toString());
			}
		}
	}

	private void addWebsiteAndAccount(User user) {
		System.out.println("Add Website and Account: ");
		String websiteName = getStringInput("Enter the website name");
		String userName = getStringInput("Enter the user name");
		String email = getStringInput("Enter the email");
		String phone = getStringInput("Enter the phone");
		String password = getStringInput("Enter the password");  // todo, check the tpye

		Website website = new Website(websiteName);
		Account account = new Account(website, userName, email, phone, password);
		website.addAccount(account);
		user.addWebsite(website);
		System.out.println("Website: " + websiteName + " added.");
	}

	private void deleteWebsite(User user) {
		System.out.println("Delete Website: ");
		String websiteName = getStringInput("Enter the website name");
		boolean isSuccess = user.deleteWebsite(websiteName);
		if (isSuccess) {
			System.out.println("Website deleted.");
		} else {
			System.out.println("Error deleting website.");
		}
	}

	private void addTestUsers() throws Exception {
		// for testing the login -> enter username and password.


		//	Add users
		User charles = new User("Charles", "Password123456".hashCode(), "Password123456");
		users.add(charles);
		User christine = new User("Christine", "SuperSecure".hashCode(), "SuperSecure");
		users.add(christine);
		User yujia = new User("Yujia", "!@#$%^&*()".hashCode(), "!@#$%^&*()");
		users.add(yujia);
		User lei = new User("Lei", "null".hashCode(), "null");
		users.add(lei);

		Website w1 = new Website("Canvas");
		Website w2 = new Website("Youtube");
		Website w3 = new Website("Spotify");

		Account account1 = new Account(w1, "charles321", "myemail@gmail.com", "",
				"THISISASUPERLONGPASSWORDthatwillgoONFORAveryLOOOOOOOOONGtimeTOSTRESSTESTtheSystem!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
						+ "fgviwyvn97934t97nf9a9737qby2v73byt07fytv0nfyvn098yfn0vn3hrrefhvn734yvtn734yvtn4783yvtn3278ytvn08y7reghuisdhuigyw"
						+ "uhfoiuwehvgibuoiaweghuihbdipuvghuweupghpaweousdhvweuhfweuafhe9hfweufhwe98fhaew9ufhe0puswdhfuiwewbh09fgwe9afhgwe79gh"
						+ "awe8jhfopweifhopwehfpweohbgopuwe4hguoibh437yt34760th2ptuobuY&*TT&*T*GB#@7G)*gb08g38rbf8byuib2yub3bfiyuwegfGT^&*FIYv");
		Account account2 = new Account(w2, "", "", "", "WhatIfAllTheFieldsAreEmptyButThis");
		Account account3 = new Account(w2, "ADifferentAccount", "", "", "ButWithDifferentInfo");

		w1.addAccount(account1);
		w2.addAccount(account2);
		w2.addAccount(account3);

		charles.addWebsite(w1);
		christine.addWebsite(w2);
		yujia.addWebsite(w3);

		w3.addAccount(new Account(w3, "A", "Duplicate", "Account", "ShouldWork"));
		w3.addAccount(new Account(w3, "A", "Duplicate", "Account", "ShouldWork"));
		w3.addAccount(new Account(w3, "A", "Duplicate", "Account", "ShouldWork"));

		System.out.println("Test data added successfully.");
	}

	private void testPasswordGenerator() {
		Generator generator = new Generator();

		System.out.println("Testing auto generator.");
		String autoPassword = generator.autoGenerator();
		System.out.println(autoPassword);

		System.out.println("manual generate a password");
		String manual_password = generator.manualGenerator();
		System.out.println(manual_password);
	}

	private void saveAllUsers() {
		for (User u: users) {
			FileUtil.fileUtil.writeUserInfo(u);
		}
		FileUtil.fileUtil.writeUsers(users);
	}

}

