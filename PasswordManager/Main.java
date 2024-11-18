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


// // manually test, no need to enter the user info, default read and create users.
//public class Main {
//	//	List of users
//	private List<User> users;
//	private Scanner scanner;
//
//	//	Constructor
//	public Main() {
//		users = new ArrayList<>();
//		scanner = new Scanner(System.in);
//	}
//
//	public static void main (String[] args) throws Exception {
//		//	Create and run instance
//		Main m = new Main();
//		m.run();
//	}
//
//	/**	Runs the program	*/
//	private void run() throws Exception {
//		//	Initialize fileutil and read users
//		System.out.println("Initializing FileUtil and reading existing users.");
//		FileUtil.init();
//		FileUtil.fileUtil.readUsers(users);
//
//		for (User u : users)
//			FileUtil.fileUtil.readUserInfo(u);
//		System.out.println();
//
//		// auto test, no need to enter the username and password.
//		System.out.println("Adding test users.");
//		addTestUsers();
//
//		System.out.println("\n========== USERS ===========");
//		for (User u: users) {
//			System.out.println("User: " + u.getUsername());
//			for (Website website : u.getWebsites()) {
//				System.out.println(" Website: " + website.getName());
//				for (Account account : website.getAccounts()) {
//					System.out.println("  Account: " + account.toString());
//				}
//			}
//		}
//
//		System.out.println("\n========PASSWORD GENERATOR TEST ==========");
//		testPasswordGenerator();
//
//		System.out.println("\nSaving all users.");
//		saveAllUsers();
//	}
//
//	private void save() {
//		FileUtil.fileUtil.writeUsers(users);
//	}
//
//	private void saveUser(User u) {
//		FileUtil.fileUtil.writeUserInfo(u);
//	}
//
//
//	private void addTestUsers() throws Exception {
//		//	Add users
//		User charles = new User("Charles", "Password123456".hashCode(), "Password123456");
//		users.add(charles);
//		User christine = new User("Christine", "SuperSecure".hashCode(), "SuperSecure");
//		users.add(christine);
//		User yujia = new User("Yujia", "!@#$%^&*()".hashCode(), "!@#$%^&*()");
//		users.add(yujia);
//		User lei = new User("Lei", "null".hashCode(), "null");
//		users.add(lei);
//
//		Website w1 = new Website("Canvas");
//		Website w2 = new Website("Youtube");
//		Website w3 = new Website("Spotify");
//
//		Account account1 = new Account(w1, "charles321", "myemail@gmail.com", "",
//				"THISISASUPERLONGPASSWORDthatwillgoONFORAveryLOOOOOOOOONGtimeTOSTRESSTESTtheSystem!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
//						+ "fgviwyvn97934t97nf9a9737qby2v73byt07fytv0nfyvn098yfn0vn3hrrefhvn734yvtn734yvtn4783yvtn3278ytvn08y7reghuisdhuigyw"
//						+ "uhfoiuwehvgibuoiaweghuihbdipuvghuweupghpaweousdhvweuhfweuafhe9hfweufhwe98fhaew9ufhe0puswdhfuiwewbh09fgwe9afhgwe79gh"
//						+ "awe8jhfopweifhopwehfpweohbgopuwe4hguoibh437yt34760th2ptuobuY&*TT&*T*GB#@7G)*gb08g38rbf8byuib2yub3bfiyuwegfGT^&*FIYv");
//		Account account2 = new Account(w2, "", "", "", "WhatIfAllTheFieldsAreEmptyButThis");
//		Account account3 = new Account(w2, "ADifferentAccount", "", "", "ButWithDifferentInfo");
//
//		w1.addAccount(account1);
//		w2.addAccount(account2);
//		w2.addAccount(account3);
//
//		charles.addWebsite(w1);
//		christine.addWebsite(w2);
//		yujia.addWebsite(w3);
//
//		w3.addAccount(new Account(w3, "A", "Duplicate", "Account", "ShouldWork"));
//		w3.addAccount(new Account(w3, "A", "Duplicate", "Account", "ShouldWork"));
//		w3.addAccount(new Account(w3, "A", "Duplicate", "Account", "ShouldWork"));
//
//		System.out.println("Test data added successfully.");
//	}
//
//	private void testPasswordGenerator() {
//		Generator generator = new Generator();
//
//		System.out.println("Testing auto generator.");
//		String autoPassword = generator.autoGenerator();
//		System.out.println(autoPassword);
//
//		System.out.println("manual generate a password");
//		String manual_password = generator.manualGenerator();
//		System.out.println(manual_password);
//	}
//
//	private void saveAllUsers() {
//		for (User u: users) {
//			FileUtil.fileUtil.writeUserInfo(u);
//		}
//		FileUtil.fileUtil.writeUsers(users);
//	}
//}



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
		// Manually add an user and website accounts.
		addTestUsers();

		// Save the user and website accounts to the file
		saveAllUsers(users);

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
			System.out.println("1. View Websites and Accounts");
			System.out.println("2. Add a Websites and Accounts");
			System.out.println("3. Delete a Websites");
			System.out.println("4. Save the account");
			System.out.println("5. Exit");

			int choice = getIntInput("Enter your choice: ");
			switch (choice) {
				case 1:
					viewWebsiteAndAccount(currentUser);
					System.out.println();
					break;
				case 2:
					addWebsiteAndAccount(currentUser);
					System.out.println();
					break;
				case 3:
					deleteWebsite(currentUser);
					System.out.println();
					break;
				case 4:
					saveAllUsers(users);
					System.out.println();
					break;
				case 5:
					System.exit(0);
				default:
					System.out.println("Invalid choice. Try again.");
					System.out.println();
			}
		}
	}

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
		System.out.println("Login or create a new user: ");
		System.out.println("1. Login to the existing user, type 'exit' to return to the main menu.");
		System.out.println("2. Create a new user");
//		System.out.println("3. Delete a user");  todo, seems like we don't have a delete user feature
		System.out.println("3. Exit");

		int choice = getIntInput("Enter your choice: ");
		switch (choice) {
			case 1:
				System.out.println();
				return loginUser();
			case 2:
				System.out.println();
				return createUser();
			case 3:
				System.out.println();
				System.exit(0);
			default:
				System.out.println();
				System.out.println("Invalid choice. Try again.");
				return userLoginOrCreate();
		}
	}

	/**	Existing user login, 5 times attempt, if all failed, return to the main menu.	*/
	private User loginUser() throws Exception {
		System.out.println("Logging in... ");
		final int MAX_ATTEMPTS = 5;
		int attempts = 0;

		while (attempts < MAX_ATTEMPTS) {
			String username = getStringInput("Username: ");

			if (username.equalsIgnoreCase("exit")) {
				System.out.println("Exiting login. Returning to main menu.");
				System.out.println();
				return userLoginOrCreate();
			}

			String password = getStringInput("Password: ");
			for (User user: users) {
				if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
					System.out.println("Login successful.");
					return user;
				}
			}

			attempts++;
			System.out.println("Invalid username. Attempt left: " + (MAX_ATTEMPTS - attempts));
		}

		System.out.println("Maximum login attempts reached. Returning to the main menu.");
		return userLoginOrCreate();
	}

	private boolean isUserExists(String username) {
		for (User user: users) {
			if (user.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	private User createUser() throws Exception {
		Generator generator = new Generator();

		System.out.println("Create a new user:");
		String username = getStringInput("Username: ");

		while (true) {
			if (isUserExists(username)) {
				System.out.println("Username already exists. Try again.");
				System.out.println();
				return createUser();
			} else {
				break;
			}
		}

		System.out.println("Password: ");
		System.out.println("Do you want to manually input a password, or auto generate one?");
		System.out.println("1. manually inpt a password");
		System.out.println("2. auto generate one");
		System.out.println("3. Return to the main menu");
		System.out.println("4. Exit the password manager");

		String password = null;
		int choice = getIntInput("Enter your choice: ");
		switch (choice) {
			case 1:
				password = generator.manualGenerator();
				System.out.println();
				break;
			case 2:
				password = generator.autoGenerator();
				System.out.println();
				break;
			case 3:
				System.out.println();
				return userLoginOrCreate();
			case 4:
				System.out.println();
				System.exit(0);
			default:
				System.out.println("Invalid choice. Try again.");
				System.out.println();
				return createUser();
		}

		if (password != null) {
			User newUser = new User(username, password);  // todo, the what should be passed in for password.
			users.add(newUser);
			System.out.println("New user created.");
			return newUser;
		} else {
			System.out.println("Generator failed to generate a password. Try again.");
			return null;
		}
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
		String websiteName = getStringInput("Enter the website name: ");
		String userName = getStringInput("Enter the user name: ");
		String email = getStringInput("Enter the email: ");
		String phone = getStringInput("Enter the phone: ");
		String password = getStringInput("Enter the password: ");  // todo, check the tpye

		Website website = new Website(websiteName);
		Account account = new Account(website, userName, email, phone, password);
		website.addAccount(account);
		user.addWebsite(website);
		System.out.println("Website: " + websiteName + " added.");
	}

	private void deleteWebsite(User user) {
		System.out.println("Delete Website: ");
		String websiteName = getStringInput("Enter the website name: ");
		boolean isSuccess = user.deleteWebsite(websiteName);
		if (isSuccess) {
			System.out.println("Website deleted.");
		} else {
			System.out.println("Error deleting website.");
		}
	}

	private void saveAllUsers(List<User> users) {
		FileUtil.init();
		for (User user : users) {
			FileUtil.fileUtil.writeUserInfo(user);
		}
		FileUtil.fileUtil.writeUsers(users);
	}

	/** Manually add users for easy testing. */
	private void addTestUsers() throws Exception {
		User user1 = new User("admin", "Password123456");
		users.add(user1);

		Website w1 = new Website("Canvas");
		Website w2 = new Website("Youtube");
		Website w3 = new Website("Spotify");

		Account account1 = new Account(w1, "admin", "admin@gmail.com", "",
				"1234");

		w1.addAccount(account1);
		w2.addAccount(account1);
		w3.addAccount(account1);

		user1.addWebsite(w1);
		user1.addWebsite(w2);
		user1.addWebsite(w3);

		System.out.println("Test data added successfully.");
	}

}
