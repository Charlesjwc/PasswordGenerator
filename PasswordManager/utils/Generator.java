package utils;

import java.util.Random;
import java.util.Scanner;

public class Generator {
    public String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public String lowercase = uppercase.toLowerCase();
    public String digits = "0123456789";
    public String special = "_-!@#$%^&*()";

    public void Generator() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("Type 1 to generate a password automatically");
            System.out.println("Type 2 to generate a password manually");
            choice = scanner.nextInt();

            if (choice == 1) {
                autoGenerator();
                break;
            } else if (choice == 2) {
                manualGenerator();
                break;
            } else {
                System.out.println("Invalid choice");
            }
        }
    }

    public String autoGenerator() {
        final int PASSWORD_LEN = 16;
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
//        System.out.println(password.toString());
        return password.toString();
    }

    public void manualGenerator() {
        Scanner scanner = new Scanner(System.in);
        String password;

        while (true) {
            System.out.println("Now input your password manually: ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                System.out.println("Your password is saved");
                break;
            } else {
                System.out.println("Your password is not valid. Please try again.");
            }
        }
        return password;
    }

    public boolean isValidPassword(String password) {
        String allowedChars = "[a-zA-Z0-9 _\\-!@#$%^&*()]*";
        return password.matches(allowedChars);
    }

    public static void main(String[] args) {
        System.out.println("start");
        Generator generator = new Generator();
    }

}
