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
		
		//	Print users
		System.out.println("Users:");
		for (User user: users) {
			System.out.println(user.getUsername());
		}
		System.out.println(users.size());
		
		//	Save file before closing
		save();
	}
	
	private void save() {
		FileUtil.fileUtil.writeUsers(users);
	}
}

