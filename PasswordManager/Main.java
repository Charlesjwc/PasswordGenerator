//	Import utils package including User, Website, Password, etc.
import utils.*;

import java.utils.*;

/**
 * 	Main.java
 * 	Main class for PasswordManager project
 * 	
 * 	@author	Charles Chang
 * 	@author	Christine Ryan
 * 	@author	Lei Hao
 * 	@author Yujia
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
		FileUtil.init();	//	Initialize FileUtil
		FileUtil.fileUtil.readUsers(users);		//	Initialize users list
	}
}

