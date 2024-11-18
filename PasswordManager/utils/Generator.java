package utils;

import java.util.Random;
import java.util.Scanner;
/**	Generator class for password manager. Generates a random password
 */
public class Generator {
	/*	Field variables	*/
    public String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public String lowercase = uppercase.toLowerCase();
    public String digits = "0123456789";
    public String special = "_-!@#$%^&*()";
    
    //	Standard password length
    private final int PASSWORD_LEN = 16;
	
	/*	Constructor	*/
    public void Generator() {
        int choice = Prompt.getInt("Type 1 to generate a password automatically\n" + 
							"Type 2 to generate a password manually\n", 1, 2);
		if (choice == 1) {
			autoGenerator();
		} else {
			manualGenerator();
        }
    }

	/**	Automatically genereates a password
	 * 	@return randomly generated password
	 */
    public String autoGenerator() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // ensure that each password contains a lowercase, an uppercase, a digit, a symbol
        password.append(uppercase.charAt(random.nextInt(uppercase.length())));
        password.append(lowercase.charAt(random.nextInt(lowercase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        String allChars = uppercase + lowercase + digits + special;

        for (int i = 4; i < PASSWORD_LEN; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
		//    System.out.println(password.toString());
        return password.toString();
    }
	
	/**	Manually enter in a password, and make sure the password is valid	*/
    public String manualGenerator() {
		String password;
		
        while (true) {
            password = Prompt.getString("Now input your password manually");
            //	Only save password if password is valid, otherwise, keep asking
            if (isValidPassword(password)) {
                System.out.println("Your password is saved");
                break;
            } else {
                System.out.println("Your password is not valid. Please try again.");
            }
        }
        return password;
    }
	
	/**	Checks whether a given String is a valid password
	 * 	@param	password to check
	 * 	@return true if password is valid, false otherwise
	 */
    public static boolean isValidPassword(String password) {
        String allowedChars = "[a-zA-Z0-9 _\\-!@#$%^&*()]*";
        return password.matches(allowedChars);
    }

	/*	Test	*/
    public static void main(String[] args) {
        System.out.println("start");
        Generator generator = new Generator();
    }

}
