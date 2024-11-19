package utils;
import utils.*;
import exceptions.*;

import java.util.*;
import java.io.*;

public class FileUtil {
	//	Filepath to info on accounts
	public static final String USERS_PATH = "files/userInfos.txt";
	
	public static final int TRASH_MAX = 10;
	
	/*	File contains passwords in the following form:
	 * 	The username, a single #, then the hashed password
	 * 	There should be no # other than in between the username and
	 * 	the hashed password
	 * 	
	 * 	Username1#HashedPassword1
	 * 	Username2#HashedPassword2
	 * 	(empty line at end)
	 */
	/*	Account files contain passwords in the following form with
	 * 	random amounts of pound symbols:
	 * 	
	 * 	<WEB>#####<ServiceName1>#####
	 * 	<ACC>###<USER><Username1>###<EMAIL><Email1>###<PASS><Password1>###
	 * 	<ACC>######<PHONE><Phone2>######<PASS><Password2>######
	 * 	<WEB>#<ServiceName2>#
	 *	<ACC>#<USER><Username3>#<EMAIL><Email3>#<PHONE><Phone3>#<PASS><Password3>#
	 * 	...
	 */
	
	//	Ensure that only one fileUtil gets instanciated
	public static FileUtil fileUtil = null;
	
	//	Private constructor to prevent duplicates
	protected FileUtil() {}
	
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
				String username = line.substring(0, line.indexOf("#"));
				//	Try to read hashed passcode, throwing an error
				//	If hashed passcode is not an int
				int hashPass = 0;
				try {
					hashPass = Integer.parseInt(line.substring(line.indexOf("#") + 1));
				}
				catch (NumberFormatException e) {
					System.out.println("ERROR: Unexpected # in '" + USERS_PATH + "'");
				}
				
				try {
					//	Create new user and add to list, unhashedPass is empty for now
					users.add(new User(username, hashPass, ""));
				}
				catch (DuplicateUsernameException e) {
					//	Ignore for now, but in the future, maybe ask the user
					//	whether they want to replace or ignore the new use
					e.printStackTrace();
				}
				
				//	Read next line
				line = reader.readLine();
			}
			//	Close reader
			in.close();
		}
		catch (IOException e) {
			System.out.println("ERROR: Failed to read '" + USERS_PATH + "', a new file will be created upon saving");
		}
	}
	
	/**	Write user info onto the file
	 * 	@param	List of users to rewrite
	 */
	public void writeUsers(List<User> users) {
		//	Let user know that file is saving
		System.out.println("Saving to " + users.size() + " users to file");
		try {
			//	Try and find file and create writer
			FileWriter out = new FileWriter(USERS_PATH);
			BufferedWriter writer = new BufferedWriter(out);
			
			//	Print all users to file
			for (User user: users) {
				writer.write(user.getUsername() + "#" + user.getHashedPassword() + "\n");
				//	System.out.print(user.getUsername() + "#" + user.getHashedPassword() + "\n");
			}
			
			//	Close writer
			writer.flush();
			out.close();
			
			System.out.println("Saved " + users.size() + " users to file");
		}
		catch (IOException e) {
			System.out.println("ERROR: Failed to write to '" + USERS_PATH + "'");
		}
	}
	
	/**	Write account info of a user onto their file
	 * 	@param User to save the info of
	 */
	public void writeUserInfo(User u) {
		//	Let user know that file is saving
		System.out.println("Saving to " + u.getUsername() + "'s info to file");
		try {
			//	Try and find file and create writer
			FileWriter out = new FileWriter(u.getFilePath());
			BufferedWriter writer = new BufferedWriter(out);

			//	Write login info by website
			for (Website w: u.getWebsites()) {
				//	<WEB> to mark that this line is a website
				writer.write("<WEB>");
				
				System.out.println("Saving accounts for " + w.getName());

				//	Random amount of pounds between 0 and key length
				int random = (int)(Math.random() * TRASH_MAX) + 1;
				StringBuilder junk = new StringBuilder();
				for (int i = 0; i < random; i++) {
					junk.append('#');
				}
				//	Print prefix<name>suffix
				writer.write(junk.toString() + "<" + w.getName() 
									+ ">" + junk.toString() + "\n");	

				//	Write each account info after website, 1 per line
				for (Account a: w.getAccounts()) {
					//	<ACC> to mark that this line is a website
					writer.write("<ACC>");

					//	Random amount of pounds between 0 and key length
					random = (int)(Math.random() * TRASH_MAX) + 1;
					junk = new StringBuilder();
					for (int i = 0; i < random; i++) {
						junk.append('#');
					}

					//	Print prefix<name>suffix
					writer.write(
						  junk.toString() + "<USER><" + a.getUsername() + ">"
						+ junk.toString() + "<EMAIL><" + a.getEmail() + ">"
						+ junk.toString() + "<PHONE><" + a.getPhone() + ">"
						+ junk.toString() + "<PASS><" + a.getPassword() + ">"
						+ junk.toString() + "\n");
				}
				
				writer.flush();
			}

			//	Close writer
			out.close();
		}
		catch (IOException e) {
			System.out.println("ERROR: Failed to write to '" + u.getFilePath() + "'");
		}
	}
	
	/**	Read account info of a user onto their file
	 * 	@param User to write the info of
	 */
	public void readUserInfo(User u) throws UserPassNullException, FileFormatException {
		//	Only read file if user has entered unhashed password
		if (u.getPassword() == null || u.getPassword().length() == 0) {
			throw new UserPassNullException(u.getUsername());
		}
		try {
			//	Try and find file and create writer
			FileReader in = new FileReader(u.getFilePath());
			BufferedReader reader = new BufferedReader(in);
			
			// Keep reading until no lines left
			String line = reader.readLine();
			Website currentSite = null;
			while (line != null) {
				//	Type of info on line
				String type = line.substring(0, 5);
				//	If website
				if (type.equals("<WEB>")) {
					//	Remove prefix
					line = line.substring(5);
					currentSite = new Website(line.substring(
							line.indexOf('<') + 1, line.indexOf('>')));
					u.addWebsite(currentSite);
				}
				//	If account
				else if (line.equals("<ACC>")) {
					//	Check that this account belongs to a website
					if (currentSite == null)
						throw new UnexpectedInputException("<WEB>", "<ACC>");
					
					//	Remove prefix
					line = line.substring(5);
					
					//	Account info
					String username = "";
					String email = "";
					String phone = "";
					String pass = "";
					
					//	Check for username
					if (line.substring(line.indexOf('<') + 1,
								line.indexOf('>')).equals("USER")) {
						line = line.substring(line.indexOf('>') + 1);
						username = line.substring(line.indexOf('<') + 1,
													line.indexOf('>'));
						line = line.substring(line.indexOf('>') + 1);
					}
					//	Check for email
					if (line.substring(line.indexOf('<') + 1,
								line.indexOf('>')).equals("EMAIL")) {
						line = line.substring(line.indexOf('>') + 1);
						email = line.substring(line.indexOf('<') + 1,
													line.indexOf('>'));
						line = line.substring(line.indexOf('>') + 1);
					}
					//	Check for phone
					if (line.substring(line.indexOf('<') + 1,
								line.indexOf('>')).equals("PHONE")) {
						line = line.substring(line.indexOf('>') + 1);
						phone = line.substring(line.indexOf('<') + 1,
													line.indexOf('>'));
						line = line.substring(line.indexOf('>') + 1);
					}
					//	Password
					//	If <PASS> not found, there was an error
					if (!line.substring(line.indexOf('<') + 1,
								line.indexOf('>')).equals("PASS")) {
						throw new MissingPasswordException(u.getUsername(), currentSite.getName());
					}
					line = line.substring(line.indexOf('>') + 1);
					pass = line.substring(line.indexOf('<') + 1, line.indexOf('>'));
					
					//	Add account to website
					currentSite.addAccount(new Account(currentSite, username, email, phone, pass));
				}
				//	If neither, there was an error
				else {
					System.out.println("Error with reading file '" + 
						u.getFilePath() + "'. Password may be incorrect.");
					return;
				}
				
				//	Read next line
				line = reader.readLine();
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("ERROR: Failed to read from '" + u.getFilePath() + "'");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}





