package utils;

import utils.exceptions.DuplicateUsernameException;
import utils.exceptions.FileFormatException;
import utils.exceptions.UserPassNullException;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.List;


public class PasswordManagerGUI implements MouseListener{
    private JFrame frame;
    private JPanel panel;
    private List<User> users;
    private User currentUser;
    private boolean isEditing = false;
    private boolean isSaved = true;
    private JTextField websitename;
    private JTextField username;
    private JTextField password;
    private JTextField email;
    private JTextField phone;
    private DefaultMutableTreeNode root;

    public PasswordManagerGUI() {

        users = new ArrayList<>();
        currentUser = null;
        FileUtil.init();
        FileUtil.fileUtil.readUsers(users);
        for (User user : users) {
            System.out.println(user.getUsername());
        }

        try {
            addTestUsers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        runGUI();
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
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(70, 30, 70, 30));

        JLabel welcomeLabel = new JLabel("Welcome to the Password Manager! ", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loginPanel.add(welcomeLabel, BorderLayout.NORTH);

        // buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton loginButton = new JButton("Login");
        JButton createNewUserButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");

        // padding the inner button to set the size of the buttons
        loginButton.setMargin(new Insets(13, 20, 13, 20));
        createNewUserButton.setMargin(new Insets(13, 20, 13, 20));
        exitButton.setMargin(new Insets(13, 20, 13, 20));

        // add the buttons to the buttonPanel
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(loginButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(createNewUserButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createHorizontalGlue());

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

        loginPanel.add(buttonPanel, BorderLayout.CENTER);

        panel.add(loginPanel, "Login");
        CardLayout cardLayout = (CardLayout) panel.getLayout();
        cardLayout.show(panel, "Login");
    }

    private void userLogin() {
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // login prompt
        JLabel loginPrompt = new JLabel("Please Login...", SwingConstants.LEFT);
        loginPrompt.setFont(new Font("Arial", Font.BOLD, 18));
        JPanel promptPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        promptPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        promptPanel.add(loginPrompt);
        loginPanel.add(promptPanel, BorderLayout.NORTH);

        // form
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // first row: username
        JLabel usernameLabel = new JLabel("Username: ");
        usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JTextField username = new JTextField(10);
        formPanel.add(usernameLabel);
        formPanel.add(username);

        // second row: password
        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JPasswordField password = new JPasswordField(10);
        formPanel.add(passwordLabel);
        formPanel.add(password);

        loginPanel.add(formPanel, BorderLayout.CENTER);

        // buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        Dimension buttonSize = new Dimension(185, 45);
        loginButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        loginPanel.add(buttonPanel, BorderLayout.SOUTH);

        password.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameText = username.getText();
                String passwordText = new String(password.getPassword());

                for (User user : users) {
                    if (user.getUsername().equals(usernameText) && user.isPassword(passwordText)) {
                        currentUser = user;
                        JOptionPane.showMessageDialog(null, "Successfully logged in!");
                        try {
                            FileUtil.fileUtil.readUserInfo(user);
                        } catch (UserPassNullException ex) {
                            throw new RuntimeException(ex);
                        } catch (FileFormatException ex) {
                            throw new RuntimeException(ex);
                        }
                        userDashboard();
                        return;
                    }
                }

                JOptionPane.showMessageDialog(null, "Invalid Username or Password. Please Try Again.");
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
        JPanel createNewUserPanel = new JPanel(new BorderLayout());
        createNewUserPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // register prompt
        JLabel resgisterPrompt = new JLabel("Please Register a New Account...", SwingConstants.LEFT);
        resgisterPrompt.setFont(new Font("Arial", Font.BOLD, 18));
        JPanel promptPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        promptPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        promptPanel.add(resgisterPrompt);
        createNewUserPanel.add(promptPanel, BorderLayout.NORTH);

        // form
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // first row: username
        JLabel usernameLabel = new JLabel("Username: ");
        usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JTextField username = new JTextField(10);
        formPanel.add(usernameLabel);
        formPanel.add(username);

        // second row: password
        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPasswordField password = new JPasswordField(10);
        formPanel.add(passwordLabel);
        formPanel.add(password);

        createNewUserPanel.add(formPanel, BorderLayout.CENTER);

        // buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton CreateNewUserButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        Dimension buttonSize = new Dimension(185, 45);
        CreateNewUserButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        buttonPanel.add(CreateNewUserButton);
        buttonPanel.add(backButton);

        createNewUserPanel.add(buttonPanel, BorderLayout.SOUTH);

        // actions
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
        JFrame dashboardFrame = new JFrame("Dashboard - Your Accounts - " + "Welcome, " + currentUser.getUsername());
        dashboardFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        dashboardFrame.setSize(600, 400);
        dashboardFrame.setLocationRelativeTo(null);  // window shows in the center

        // main panel
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 15, 30));

        // websites on the left side, read from the user info(not from a file) todo
        root = new DefaultMutableTreeNode("Websites");
        Map<String, DefaultMutableTreeNode> websiteNodes = new HashMap<>();
        for (Website website : currentUser.getWebsites()) {
            DefaultMutableTreeNode websiteNode = new DefaultMutableTreeNode(website.getName());
            for (Account account : website.getAccounts()) {
                websiteNode.add(new DefaultMutableTreeNode(account.getUsername()));
            }
            root.add(websiteNode);
            websiteNodes.put(website.getName(), websiteNode);
        }

        JTree websites = new JTree(root);

        JPanel treePanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(websites);
        scrollPane.setPreferredSize(new Dimension(200, 0));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        treePanel.add(scrollPane, BorderLayout.CENTER);

        // bottom + and -
        JPanel treeButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        treeButtonPanel.setPreferredSize(new Dimension(200, 30));
        treeButtonPanel.setBackground(treePanel.getBackground());

        JButton addTreeButton = new JButton("+");
        addTreeButton.setPreferredSize(new Dimension(50, 30));
        JButton deleteTreeButton = new JButton("-");
        deleteTreeButton.setPreferredSize(new Dimension(50, 30));

        addTreeButton.setMargin(new Insets(0, 0, 0, 0));
        deleteTreeButton.setMargin(new Insets(0, 0, 0, 0));

        addTreeButton.setFocusPainted(false);
        addTreeButton.setBorderPainted(false);
        addTreeButton.setContentAreaFilled(false);

        deleteTreeButton.setFocusPainted(false);
        deleteTreeButton.setBorderPainted(false);
        deleteTreeButton.setContentAreaFilled(false);

        treeButtonPanel.add(addTreeButton);
        treeButtonPanel.add(deleteTreeButton);

        treePanel.add(treeButtonPanel, BorderLayout.SOUTH);
        treePanel.setBorder(BorderFactory.createTitledBorder(""));
        dashboardPanel.add(treePanel, BorderLayout.WEST);

        // info panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 5, 2));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 10));

        // website name
        JLabel websitenameLabel = new JLabel("Website: ");
        websitename = new JTextField();
        websitename.setEnabled(false);
        infoPanel.add(websitenameLabel);
        infoPanel.add(websitename);

        // username
        JLabel usernameLabel = new JLabel("Username: ");
        username = new JTextField();
        username.setEnabled(false);
        infoPanel.add(usernameLabel);
        infoPanel.add(username);

        // password
        JLabel passwordLabel = new JLabel("Password: ");
        password = new JTextField();
        password.setEnabled(false);
        infoPanel.add(passwordLabel);
        infoPanel.add(password);

        // email
        JLabel emailLabel = new JLabel("Email: ");
        email = new JTextField();
        email.setEnabled(false);
        infoPanel.add(emailLabel);
        infoPanel.add(email);

        // phone
        JLabel phoneLabel = new JLabel("Phone: ");
        phone = new JTextField();
        phone.setEnabled(false);
        infoPanel.add(phoneLabel);
        infoPanel.add(phone);

        rightPanel.add(infoPanel, BorderLayout.CENTER);

        // bottom button -> Auto Generate Password, Save, logout
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 10, 2));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 7, 5));

        JButton editAccountButton = new JButton("Edit");
        JButton saveAccountButton = new JButton("Save");
        JButton generatePasswordButton = new JButton("Auto Generate Password");
        JButton logoutButton = new JButton("Logout");

        saveAccountButton.setVisible(false);
        editAccountButton.setVisible(false);
        generatePasswordButton.setEnabled(false);

        generatePasswordButton.setPreferredSize(new Dimension(200, 40));
        logoutButton.setPreferredSize(new Dimension(200, 40));
        saveAccountButton.setPreferredSize(new Dimension(200, 35));


        bottomPanel.add(editAccountButton);
        bottomPanel.add(saveAccountButton);
        bottomPanel.add(generatePasswordButton);
        bottomPanel.add(logoutButton);

        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
        dashboardPanel.add(rightPanel, BorderLayout.CENTER);

        websites.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) { DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) websites.getLastSelectedPathComponent();

                // If no node or root node is selected, hide the edit button and clear fields
                if (selectedNode == null || selectedNode.isRoot()) {
                    editAccountButton.setVisible(false);  // Hide edit button
                    clearFields();  // Clear all fields
                    return;
                }

                // Check if the selected node is a website or an account
                if (selectedNode.getParent() == root) {
                    // Website node selected, hide the edit button
                    editAccountButton.setVisible(false);
                    clearFieldsExceptWebsite();  // Only clear fields except website name
                } else {
                    // Account node selected, show the edit button
                    editAccountButton.setVisible(true);
                    populateFieldsFromSelection(selectedNode);  // Populate fields for account
                }
            }
        });



        addTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user to enter a website name
                String websiteName = JOptionPane.showInputDialog(dashboardFrame,
                        "Enter the website name:",
                        "Add Website or Account",
                        JOptionPane.PLAIN_MESSAGE);

                // If the user cancels or enters no input, exit the action
                if (websiteName == null || websiteName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dashboardFrame,
                            "Website name cannot be empty.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                websiteName = websiteName.trim(); // Trim whitespace

                // Populate the website text field
                websitename.setText(websiteName);

                // Enable editing for all text fields
                enableFields(true);

                // Reset other fields
                username.setText("");
                password.setText("");
                email.setText("");
                phone.setText("");

                // Update button states
                saveAccountButton.setVisible(true);
                generatePasswordButton.setEnabled(true);
                editAccountButton.setVisible(false); // Hide edit button

                // Save the website name temporarily but don't update the tree
                JOptionPane.showMessageDialog(dashboardFrame,
                        "Enter account details and click 'Save' to add the website and account.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });



        editAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isEditing = true; // Flag to indicate editing mode
                isSaved = false; // Changes are not yet saved
                enableFields(true); // Enable the text fields
                generatePasswordButton.setEnabled(true);
                saveAccountButton.setVisible(true);
                editAccountButton.setVisible(false);
            }
        });

        saveAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath[] expandedPaths = saveExpandedPaths(websites);

                // Gather input data from fields
                String savedWebsiteName = websitename.getText().trim();
                String savedUsername = username.getText().trim();
                String savedPassword = password.getText().trim();
                String savedEmail = email.getText().trim();
                String savedPhone = phone.getText().trim();

                // Validate required fields
                if (savedWebsiteName.isEmpty() || savedUsername.isEmpty() || savedPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(dashboardFrame,
                            "Please fill in all required fields (Website, Username, Password).",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DefaultMutableTreeNode websiteNode = null;
                boolean isExistingWebsite = false;

                // Check if the website exists in the user's data
                for (Website website : currentUser.getWebsites()) {
                    if (website.getName().equals(savedWebsiteName)) {
                        websiteNode = findWebsiteNode(savedWebsiteName); // Find the node in the tree
                        isExistingWebsite = true;

                        // Check if the account already exists under this website
                        boolean isExistingAccount = false;
                        for (Account account : website.getAccounts()) {
                            if (account.getUsername().equals(savedUsername)) {
                                // Update existing account details
                                account.setPassword(savedPassword);
                                account.setEmail(savedEmail);
                                account.setPhone(savedPhone);
                                JOptionPane.showMessageDialog(dashboardFrame,
                                        "Account details updated successfully.",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                                isExistingAccount = true;
                                break;
                            }
                        }

                        // If the account doesn't exist, create a new account
                        if (!isExistingAccount) {
                            Account newAccount = new Account(website, savedUsername, savedEmail, savedPhone, savedPassword);
                            website.addAccount(newAccount);

                            // Add the new account to the tree
                            if (websiteNode != null) {
                                websiteNode.add(new DefaultMutableTreeNode(savedUsername));
                            }
                            JOptionPane.showMessageDialog(dashboardFrame,
                                    "New account added under the website.",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    }
                }

                // If the website doesn't exist, create a new website and account
                if (!isExistingWebsite) {
                    Website newWebsite = new Website(savedWebsiteName);
                    Account newAccount = new Account(newWebsite, savedUsername, savedEmail, savedPhone, savedPassword);
                    newWebsite.addAccount(newAccount);
                    currentUser.addWebsite(newWebsite);

                    // Add the new website and account to the tree
                    websiteNode = new DefaultMutableTreeNode(savedWebsiteName);
                    root.add(websiteNode);
                    websiteNode.add(new DefaultMutableTreeNode(savedUsername));

                    JOptionPane.showMessageDialog(dashboardFrame,
                            "New website and account created successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                }

                // Reload the tree model to reflect the changes
                ((DefaultTreeModel) websites.getModel()).reload();
                restoreExpandedPath(websites, expandedPaths);

                // Save the updated user data to file
                FileUtil.fileUtil.writeUserInfo(currentUser);

                // Reset UI state
                completeSave();
            }

            private DefaultMutableTreeNode findWebsiteNode(String websiteName) {
                Enumeration<TreeNode> children = root.children();
                while (children.hasMoreElements()) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
                    if (node.getUserObject().toString().equals(websiteName)) {
                        return node;
                    }
                }
                return null;
            }

            private void completeSave() {
                isEditing = false;
                isSaved = true;
                enableFields(false); // Disable editing fields
                editAccountButton.setVisible(false);
                saveAccountButton.setVisible(false);
                generatePasswordButton.setEnabled(false);
            }
        });

        deleteTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath[] expandedPaths = saveExpandedPaths(websites);

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) websites.getLastSelectedPathComponent();
                if (selectedNode == null || selectedNode.isRoot()) {
                    JOptionPane.showMessageDialog(dashboardFrame, "Please select a website or an account to delete",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (selectedNode.getParent() == root) {
                    clearFieldsExceptWebsite(); // Clear only non-website fields
                } else {
                    populateFieldsFromSelection(selectedNode); // Populate fields for accounts
                }

                int confirm = JOptionPane.showConfirmDialog(dashboardFrame, "Are you sure you want to delete this account?",
                        "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                    if (parentNode == root) {
                        String selectedWebsiteName = selectedNode.getUserObject().toString();
                        for (int i = 0; i < currentUser.getWebsites().size(); i++) {
                            Website website = currentUser.getWebsites().get(i);
                            if (website.getName().equals(selectedWebsiteName)) {
                                currentUser.getWebsites().remove(i);
                                break;
                            }
                        }
                        root.remove(selectedNode);
                    } else {
                        String websiteName = parentNode.getUserObject().toString();
                        for (Website website : currentUser.getWebsites()) {
                            if (website.getName().equals(websiteName)) {
                                List<Account> accounts = website.getAccounts();
                                for (int i = 0; i < accounts.size(); i++) {
                                    Account account = accounts.get(i);
                                    if (account.getUsername().equals(selectedNode.getUserObject().toString())) {
                                        accounts.remove(i);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        parentNode.remove(selectedNode);
                    }

                    ((DefaultTreeModel) websites.getModel()).reload();
                    restoreExpandedPath(websites, expandedPaths);

                    FileUtil.fileUtil.writeUserInfo(currentUser);

                    JOptionPane.showMessageDialog(dashboardFrame, "Account deleted!");
                }
            }
        });

        username.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(username.getText() != null && username.getText().length() > 0) {
                    StringSelection selection = new StringSelection(username.getText());
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                    JOptionPane.showMessageDialog( null, "Username copied to clipboard: " + username.getText());
                }
            }

        });
        password.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (password.getText() != null && password.getText().length() > 0) {
                    StringSelection selection = new StringSelection(password.getText());
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                    JOptionPane.showMessageDialog(null, "Password copied to clipboard: " + password.getText());

                }
            }
        });
        websitename.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (websitename.getText() != null && websitename.getText().length() > 0) {
                    StringSelection selection = new StringSelection(websitename.getText());
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                    JOptionPane.showMessageDialog(null, "Website name copied to clipboard: " + websitename.getText());

                }
            }
        });
        email.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (email.getText() != null && email.getText().length() > 0) {
                    StringSelection selection = new StringSelection(email.getText());
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                    JOptionPane.showMessageDialog(null, "Email copied to clipboard: " + email.getText());
                }
            }
        });
        phone.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (phone.getText() != null && phone.getText().length() > 0) {
                    StringSelection selection = new StringSelection(phone.getText());
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                    JOptionPane.showMessageDialog(null, "Phone copied to clipboard: " + phone.getText());
                }
            }
        });


        generatePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Generator generator = new Generator();

                String generatedPassword = generator.autoGenerator();
                password.setText(generatedPassword);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAllUsers(users);
                dashboardFrame.dispose();
                runGUI();
            }
        });

        dashboardFrame.add(dashboardPanel);
        dashboardFrame.setVisible(true);
        frame.dispose();
    }

    private void enableFields(boolean enable) {
        websitename.setEditable(enable);
        websitename.setEnabled(enable);
        username.setEditable(enable);
        username.setEnabled(enable);
        password.setEditable(enable);
        password.setEnabled(enable);
        email.setEditable(enable);
        email.setEnabled(enable);
        phone.setEditable(enable);
        phone.setEnabled(enable);
    }

    private void populateFields(Account account) {
        username.setText(account.getUsername());
        password.setText(account.getPassword());
        email.setText(account.getEmail());
        phone.setText(account.getPhone());
    }

    private void clearFields() {
        websitename.setText("");
        username.setText("");
        password.setText("");
        email.setText("");
        phone.setText("");
    }
    private void clearFieldsExceptWebsite() {
        username.setText("");
        password.setText("");
        email.setText("");
        phone.setText("");
    }

    private void populateFieldsFromSelection(DefaultMutableTreeNode selectedNode) {
        if (selectedNode.getParent() == root) {
            websitename.setText(selectedNode.getUserObject().toString());
            clearFieldsExceptWebsite();
        } else {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
            websitename.setText(parent.getUserObject().toString());

            String selectedUsername = selectedNode.getUserObject().toString();
            for (Website website : currentUser.getWebsites()) {
                if (website.getName().equals(parent.getUserObject().toString())) {
                    for (Account account : website.getAccounts()) {
                        if (account.getUsername().equals(selectedUsername)) {
                            populateFields(account);
                            return;
                        }
                    }
                }
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() ==  username) {
            StringSelection selection = new StringSelection(username.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            JOptionPane.showMessageDialog( null, "Username copied to clipboard: " + username);
        }
        if (e.getSource() == password) {
            StringSelection selection = new StringSelection(password.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            JOptionPane.showMessageDialog(null, "Password copied to clipboard: " + password);

        }
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }



    private void addTestUsers() throws Exception {
        User user1 = new User("admin", "1234");
        users.add(user1);

        Website w1 = new Website("Canvas");
        Website w2 = new Website("Youtube");
        Website w3 = new Website("Spotify");

        Account account1 = new Account(w1, "Admin", "admin@gmail.com", "",
                "1234");

        w1.addAccount(account1);
        w2.addAccount(account1);
        w3.addAccount(account1);

        user1.addWebsite(w1);
        user1.addWebsite(w2);
        user1.addWebsite(w3);

//        System.out.println("Test data added successfully.");
//        saveAllUsers(users);
    }

    private void saveAllUsers(List<User> users) {
        for (User user : users) {
            System.out.println(user.getUsername());   // todo, testing
            FileUtil.fileUtil.writeUserInfo(user);
        }
        FileUtil.fileUtil.writeUsers(users);
    }

    // keep the original level expanded paths
    private TreePath[] saveExpandedPaths(JTree tree) {
        int rowCount = tree.getRowCount();
        TreePath[] paths = new TreePath[rowCount];
        int index = 0;
        for (int i = 0; i < rowCount; i++) {
            if (tree.isExpanded(i)) {
                paths[index++] = tree.getPathForRow(i);
            }
        }
        return Arrays.copyOf(paths, index);
    }

    private void restoreExpandedPath(JTree tree, TreePath[] paths) {
        for (TreePath path: paths) {
            tree.expandPath(path);
        }
    }

}