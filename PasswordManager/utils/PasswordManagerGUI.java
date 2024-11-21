package utils;

import utils.exceptions.DuplicateUsernameException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PasswordManagerGUI {
    private JFrame frame;
    private JPanel panel;
    private List<User> users;
    private User currentUser;

    public PasswordManagerGUI() {
        users = new ArrayList<>();
        currentUser = null;
        FileUtil.init();
        FileUtil.fileUtil.readUsers(users);
        runGUI();
    }

    public static void main(String[] args) {
        new PasswordManagerGUI();
    }

    private void runGUI() {
        frame = new JFrame("Password Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        frame.setLocationRelativeTo(null);

        panel = new JPanel(new CardLayout());
        frame.add(panel);
        loginScreen();
        frame.setVisible(true);
    }

    private void loginScreen() {
        JPanel loginPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(110, 30, 110, 30));

//        JLabel welcomeLabel = new JLabel("Welcome to the password manager! ", SwingConstants.CENTER);
//        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
//        loginPanel.add(welcomeLabel);

        // buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton loginButton = new JButton("Login");
        JButton createNewUserButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(loginButton);
        buttonPanel.add(createNewUserButton);
        buttonPanel.add(exitButton);

        // actions
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userLogin();
            }
        });

        createNewUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewUser();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        loginPanel.add(loginButton);
        loginPanel.add(createNewUserButton);
        loginPanel.add(exitButton);

        panel.add(loginPanel, "Login");
        CardLayout cardLayout = (CardLayout) panel.getLayout();
        cardLayout.show(panel, "Login");
    }

    private void userLogin() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

//        JLabel loginPrompt = new JLabel("Please login now", SwingConstants.CENTER);
//        loginPrompt.setFont(new Font("Arial", Font.BOLD, 16));
//        loginPanel.add(loginPrompt);

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        loginPanel.add(new JLabel("Username: "));
        loginPanel.add(username);

        loginPanel.add(new JLabel("Password: "));
        loginPanel.add(password);

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        loginPanel.add(loginButton);
        loginPanel.add(backButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameText = username.getText();
                String passwordText = new String(password.getPassword());

                for (User user : users) {
                    if (user.getUsername().equals(usernameText) && user.getPassword().equals(passwordText)) {
                        currentUser = user;
                        JOptionPane.showMessageDialog(null, "Successfully logged in!");
                        userDashboard();
                        return;
                    }
                }

                JOptionPane.showMessageDialog(null, "Invalid username or password, try again later.");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginScreen();
            }
        });

        panel.add(loginPanel, "UserLogin");
        CardLayout cardLayout = (CardLayout) panel.getLayout();
        cardLayout.show(panel, "UserLogin");
    }

    private void createNewUser() {
        JPanel createNewUserPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        createNewUserPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        JButton CreateNewUserButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        createNewUserPanel.add(new JLabel("New Username: "));
        createNewUserPanel.add(username);
        createNewUserPanel.add(new JLabel("New Password: "));
        createNewUserPanel.add(password);

        createNewUserPanel.add(CreateNewUserButton);
        createNewUserPanel.add(backButton);

        CreateNewUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameText = username.getText();
                String passwordText = new String(password.getPassword());

                if (usernameText.isEmpty() || passwordText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username or password is empty!");
                    return;
                }

                for (User user : users) {
                    if (user.getUsername().equals(usernameText)) {
                        JOptionPane.showMessageDialog(null, "Username already exists!");
                        return;
                    }
                }

                try {
                    User newUser = new User(usernameText, passwordText);
                    users.add(newUser);
                    JOptionPane.showMessageDialog(null, "Successfully created a new account!");
                    userLogin();
                } catch (DuplicateUsernameException ex) {  // todo, make sure the exception is correct.
                    throw new RuntimeException(ex);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginScreen();
            }
        });

        panel.add(createNewUserPanel, "CreateNewUser");
        CardLayout cardLayout = (CardLayout) panel.getLayout();
        cardLayout.show(panel, "CreateNewUser");
    }

    private void userDashboard() {
        JPanel dashboardPanel = new JPanel(new GridLayout(5, 1, 5, 3));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dashboardPanel.add(welcomeLabel);

        JButton viewWebsitesButton = new JButton("View Websites");
        JButton addWebsiteButton = new JButton("Add Website");
        JButton deleteWebsiteButton = new JButton("Delete Website");
        JButton logoutButton = new JButton("Logout");

        viewWebsitesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewWebsites();
            }
        });

        addWebsiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWebsite();
            }
        });

        deleteWebsiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteWebsite();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentUser = null;
                loginScreen();
            }
        });

        dashboardPanel.add(viewWebsitesButton);
        dashboardPanel.add(addWebsiteButton);
        dashboardPanel.add(deleteWebsiteButton);
        dashboardPanel.add(logoutButton);

        panel.add(dashboardPanel, "Dashboard");
        CardLayout cardLayout = (CardLayout) panel.getLayout();
        cardLayout.show(panel, "Dashboard");
    }

    private void viewWebsites() {
        StringBuilder websites = new StringBuilder("Websites: \n");
        for (Website website : currentUser.getWebsites()) {
            websites.append("- ").append(website.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(frame, websites.toString());
    }

    private void addWebsite() {
        JTextField newWebsite = new JTextField();
        JTextField username = new JTextField();
        JTextField password = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();

        Object[] input = {
                "Website Name: ", newWebsite,
                "Username: ", username,
                "Password: ", password,
                "Email: ", email,
                "Phone: ", phone
        };
        int option = JOptionPane.showConfirmDialog(frame, input, "Add Website", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Website website = new Website(newWebsite.getText());
            Account account = new Account(website, username.getText(), email.getText(), phone.getText(), password.getText());
            website.addAccount(account);
            currentUser.addWebsite(website);
            JOptionPane.showMessageDialog(frame, "Successfully added a new website!");
        }
    }

    private void deleteWebsite() {
        String websiteName = JOptionPane.showInputDialog(frame, "Enter website name");
        if (websiteName != null && !websiteName.isEmpty()) {
            if (currentUser.deleteWebsite(websiteName)) {
                JOptionPane.showMessageDialog(frame, "Successfully deleted the website!");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to delete the website!");
            }
        }
    }

}