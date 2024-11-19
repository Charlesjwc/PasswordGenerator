package utils;
import utils.*;

import java.util.*;
import java.io.*;

public class SecureFileUtil extends FileUtil{
	//	Private constructor to prevent duplicates
	private SecureFileUtil() {}
	
	/**	Initializes singleton object if it doesn't already exist	*/
	public static void init() {
		if (fileUtil == null)
			fileUtil = new SecureFileUtil();
		else
			System.out.println("SecureFileUtil already exists");
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

			//	Key for encryption
			char[] key = u.getPassword().toCharArray();
			//	Index of what part of the encryption key has been reached
			int[] keyIndex = new int[]{0};

			//	Write login info by website
			for (Website w: u.getWebsites()) {
				//	<WEB> to mark that this line is a website
				writer.write(Encryptor.encrypt("<WEB>", key, keyIndex));
				
				System.out.println("Saving accounts for " + w.getName());

				//	Random amount of pounds between 0 and key length
				int random = (int)(Math.random() * key.length) + 1;
				StringBuilder junk = new StringBuilder();
				for (int i = 0; i < random; i++) {
					junk.append('#');
				}
				//	Print encrypted prefix<name>suffix
				writer.write(Encryptor.encrypt(junk.toString() + "<" + w.getName()
							+ ">" + junk.toString(), key, keyIndex) + "\n");	

				//	Write each account info after website, 1 per line
				for (Account a: w.getAccounts()) {
					//	<ACC> to mark that this line is a website
					writer.write(Encryptor.encrypt("<ACC>", key, keyIndex));

					//	Random amount of pounds between 0 and key length
					random = (int)(Math.random() * key.length) + 1;
					junk = new StringBuilder();
					for (int i = 0; i < random; i++) {
						junk.append('#');
					}

					//	Print encrypted prefix<name>suffix
					writer.write(Encryptor.encrypt(
						  junk.toString() + "<USER><" + a.getUsername() + ">"
						+ junk.toString() + "<EMAIL><" + a.getEmail() + ">"
						+ junk.toString() + "<PHONE><" + a.getPhone() + ">"
						+ junk.toString() + "<PASS><" + a.getPassword() + ">"
						+ junk.toString(), key, keyIndex) + "\n");
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
	public void readUserInfo(User u) {
		//	Only read file if user has entered unhashed password
		if (u.getPassword() == null || u.getPassword().length() == 0) {
			System.out.println("Could not read file for user '" + u.getUsername() + 
						"', password not provided");
			return;
		}
		try {
			//	Try and find file and create writer
			FileReader in = new FileReader(u.getFilePath());
			BufferedReader reader = new BufferedReader(in);

			//	Key for decryption
			char[] key = u.getPassword().toCharArray();
			//	Index of what part of the decrypt key has been reached
			int[] keyIndex = new int[]{0};
			
			// Keep reading until no lines left
			String line = Encryptor.decrypt(reader.readLine(), key, keyIndex);
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
						System.out.println("ERROR: Password not found for user "
							+ u.getUsername() + " on account for " + currentSite.getName());
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
				line = Encryptor.decrypt(reader.readLine(), key, keyIndex);
			}
		}
		catch (IOException e) {
			System.out.println("ERROR: Failed to read from '" + u.getFilePath() + "'");
		}
	}
}





