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
	private List<User> users = new ArrayList<>();
	
	//	Constructor
	public Main() {
		users = new ArrayList<>();
	}
	
	public static void main (String[] args) {
		//	Create and run instance
		Main m = new Main();
		m.run();
	}
	
	/**	Runs the program	*/
	private void run() {
		//	Initialize fileutil and read users
		FileUtil.init();
		FileUtil.fileUtil.readUsers(users);
		
		//	addTestUsers();
		
		//	Print users
		System.out.println("Users:");
		for (User user: users) {
			System.out.println(user.getUsername());
			//	saveUser(user);
			for (Website w: user.getWebsites())
				for (Account a: w.getAccounts())
					System.out.print(a);
		}
		System.out.println(users.size());
		
		//	Save file before closing
		save();
	}
	
	private void save() {
		FileUtil.fileUtil.writeUsers(users);
	}
	
	private void saveUser(User u) {
		FileUtil.fileUtil.writeUserInfo(u);
	}
	
	private void addTestUsers() {
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
		charles.addWebsite(w1);
		w1.addAccount(new Account(w1, "charles321", "myemail@gmail.com", "", 
			"THISISASUPERLONGPASSWORDthatwillgoONFORAveryLOOOOOOOOONGtimeTOSTRESSTESTtheSystem!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
			+ "fgviwyvn97934t97nf9a9737qby2v73byt07fytv0nfyvn098yfn0vn3hrrefhvn734yvtn734yvtn4783yvtn3278ytvn08y7reghuisdhuigyw"
			+ "uhfoiuwehvgibuoiaweghuihbdipuvghuweupghpaweousdhvweuhfweuafhe9hfweufhwe98fhaew9ufhe0puswdhfuiwewbh09fgwe9afhgwe79gh"
			+ "awe8jhfopweifhopwehfpweohbgopuwe4hguoibh437yt34760th2ptuobuY&*TT&*T*GB#@7G)*gb08g38rbf8byuib2yub3bfiyuwegfGT^&*FIYv"));
		
		Website w2 = new Website("Youtube");
		christine.addWebsite(w2);
		w2.addAccount(new Account(w2, "", "", "", "WhatIfAllTheFieldsAreEmptyButThis"));
		w2.addAccount(new Account(w2, "ADifferentAccount", "", "", "ButWithDifferentInfo"));
		
		Website w3 = new Website("Spotify");
		yujia.addWebsite(w3);
		w3.addAccount(new Account(w3, "A", "Duplicate", "Account", "ShouldWork"));
		w3.addAccount(new Account(w3, "A", "Duplicate", "Account", "ShouldWork"));
		w3.addAccount(new Account(w3, "A", "Duplicate", "Account", "ShouldWork"));
	}
}

