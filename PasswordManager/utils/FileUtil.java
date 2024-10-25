package utils;

import java.util.*;
import java.io.*;

public class FileUtil {
	//	Filepath to info on accounts
	public static final String USERS_PATH = "files/userInfos.txt";
	/*	File contains passwords in the following form:
	 * 	The username, a single space, then the hashed password
	 * 	There should be no spaces other than in between the username and
	 * 	the hashed password
	 * 	
	 * 	Username1 HashedPassword1
	 * 	Username2 HashedPassword2
	 * 	(empty line at end)
	 */
	
	//	Ensure that only one fileUtil gets instanciated
	public static FileUtil fileUtil = null;
	
	//	Private constructor to prevent duplicates
	private FileUtil() {}
	
	/**	Initializes singleton object if it doesn't already exist	*/
	public static void init() {
		if (fileUtil == null)
			fileUtil = new FileUtil();
		else
			System.out.println("FileUtil already exists");
	}
	
	/**	Read user info from the file
	 * 	@param	Empty list of users to add to
	 */
	public void readUsers(List<User> users) {
		try {
			//	Try to find file and create reader
			FileReader in = new FileReader(USERS_PATH);
			BufferedReader reader = new BufferedReader(in);
			
			//	Read the file, and add to users
			String line = reader.readLine();
			while (line != null) {
				//	Each line contains a user, a space, then the hashed password
				String username = line.substring(0, line.indexOf(" "));
				
				//	Try to read hashed passcode, throwing an error
				//	If hashed passcode is not an int
				int hashPass = 0;
				try {
					hashPass = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
				}
				catch (NumberFormatException e) {
					System.out.println("ERROR: Unexpected whitespace in '" + USERS_PATH + "'");
				}
				
				//	Create new user and add to list
				users.add(new User(username, hashPass));
				
				//	Read next line
				line = reader.readLine();
			}
			//	Close reader
			in.close();
		}
		catch (IOException e) {
			System.out.println("ERROR: Failed to read '" + USERS_PATH + "'");
		}
	}
	
	/**	Write user info onto the file
	 * 	@param	List of users to rewrite
	 */
	public void writeUsers(List<User> users) {
		try {
			//	Try and find file and create writer
			FileWriter out = new FileWriter(USERS_PATH);
			BufferedWriter writer = new BufferedWriter(out);
			
			//	Print all users to file
			for (User user: users) {
				writer.write(user.getName() + " " + user.getPass() + "\n");
			}
			
			//	Close writer
			out.close();
		}
		catch (IOException e) {
			System.out.println("ERROR: Failed to write to '" + USERS_PATH + "'");
		}
	}
}

