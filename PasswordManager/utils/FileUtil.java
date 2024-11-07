package utils;

import java.util.*;
import java.io.*;

public class FileUtil {
	//	Filepath to info on accounts
	public static final String USERS_PATH = "files/userInfos.txt";
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
				
				//	Create new user and add to list, unhashedPass is empty for now
				users.add(new User(username, hashPass, ""));
				
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
		try {
			//	Try and find file and create writer
			FileWriter out = new FileWriter(u.getFilePath());
			BufferedWriter writer = new BufferedWriter(out);

			//	Key for encryption
			char[] key = u.getPassword().toCharArray();
			//	Index of what part of the encryption key has been reached
			int[] keyIndex = new int[]{0};

			//	Write login info by website
			for (Website w: u.getWebsites()) {
				//	<WEB> to mark that this line is a website
				writer.write(encrypt("<WEB>", key, keyIndex));

				//	Random amount of pounds between 0 and key length
				int random = (int)(Math.random() * key.length) + 1;
				StringBuilder junk = new StringBuilder();
				for (int i = 0; i < random; i++) {
					junk.append('#');
				}
				//	Print encrypted prefix<name>suffix
				writer.write(encrypt(junk.toString() + "<" + w.getName()
							+ ">" + junk.toString(), key, keyIndex));
				System.out.println("\n");

				//	Write each account info after website, 1 per line
				for (Account a: w.getAccounts()) {
					//	<ACC> to mark that this line is a website
					writer.write(encrypt("<ACC>", key, keyIndex));

					//	Random amount of pounds between 0 and key length
					random = (int)(Math.random() * key.length) + 1;
					junk = new StringBuilder();
					for (int i = 0; i < random; i++) {
						junk.append('#');
					}

					//	Print encrypted prefix<name>suffix
					writer.write(encrypt(
						  junk.toString() + "<" + a.getUsername() + ">"
						+ junk.toString() + "<" + a.getEmail() + ">"
						+ junk.toString() + "<" + a.getPhone() + ">"
						+ junk.toString() + "<" + a.getPassword() + ">"
						+ junk.toString(), key, keyIndex));
					System.out.println("\n");
				}
			}

			//	Close writer
			out.close();
		}
		catch (IOException e) {
			System.out.println("ERROR: Failed to write to '" + u.getFilePath() + "'");
		}
	}

	/**	Encrypts the a String using a char[] key and int[] index
	 * 	@param	String	to encrypt
	 * 	@param	char[]	key for encryption
	 * 	@param	int[]	index of encryption key
	 * 	@return	String	encrypted String
	 */
	private String encrypt(String str, char[] key, int[] keyIndex) {
		//	Convert str from String to char[], then call other encrypt method
		encrypt(str.toCharArray(), key, keyIndex);
		return new String(key);  // todo
	}
	
	/**	Encrypts the a String using a char[] key and int[] index
	 * 	@param	char[]	to encrypt (ARRAY WILL BE CHANGED)
	 * 	@param	char[]	key for encryption
	 * 	@param	int[]	index of encryption key
	 * 	@return	String	encrypted String
	 */
	private String encrypt(char[] str, char[] key, int[] keyIndex) {
		//	Length of key
		int n = key.length;
		//	Encrypt str
		for (int i = 0; i < str.length; i++) {
			//	Shift char
			int shifted = str[i] + key[keyIndex[0]];
			//	Make sure char is within bounds
			if (shifted < 32)
				shifted += 95;
			if (shifted > 126)
				shifted -= 95;
			
			//	Update char
			str[i] = (char)shifted;
			
			//	Increment keyIndex
			keyIndex[0] = (keyIndex[0] + 1) % key.length;
		}
		//	Return string
		return new String(str);
	}
}





