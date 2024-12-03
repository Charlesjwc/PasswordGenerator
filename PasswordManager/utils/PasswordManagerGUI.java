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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.List;


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
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Websites");
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
        JTextField websitename = new JTextField();
        websitename.setEditable(false);
        infoPanel.add(websitenameLabel);
        infoPanel.add(websitename);

        // username
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField username = new JTextField();
        username.setEditable(false);
        infoPanel.add(usernameLabel);
        infoPanel.add(username);

        // password
        JLabel passwordLabel = new JLabel("Password: ");
        JTextField password = new JTextField();
        password.setEditable(false);
        infoPanel.add(passwordLabel);
        infoPanel.add(password);

        // email
        JLabel emailLabel = new JLabel("Email: ");
        JTextField email = new JTextField();
        email.setEditable(false);
        infoPanel.add(emailLabel);
        infoPanel.add(email);

        // phone
        JLabel phoneLabel = new JLabel("Phone: ");
        JTextField phone = new JTextField();
        phone.setEditable(false);
        infoPanel.add(phoneLabel);
        infoPanel.add(phone);

        rightPanel.add(infoPanel, BorderLayout.CENTER);

        // bottom button -> Auto Generate Password, Save, logout
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 10, 2));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 7, 5));

        JButton saveAccountButton = new JButton("Save");
        JButton generatePasswordButton = new JButton("Auto Generate Password");
        JButton logoutButton = new JButton("Logout");

        generatePasswordButton.setPreferredSize(new Dimension(200, 40));
        logoutButton.setPreferredSize(new Dimension(200, 40));
        saveAccountButton.setPreferredSize(new Dimension(200, 35));

        bottomPanel.add(saveAccountButton);
        bottomPanel.add(generatePasswordButton);
        bottomPanel.add(logoutButton);

        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
        dashboardPanel.add(rightPanel, BorderLayout.CENTER);

        websites.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) websites.getLastSelectedPathComponent();
                if (selectedNode == null || selectedNode.isRoot()) {
                    websitename.setText("");
                    username.setText("");
                    password.setText("");
                    email.setText("");
                    phone.setText("");
                    return;
                }

                if (selectedNode.getParent() == root) {
                    websitename.setText(selectedNode.getUserObject().toString());
                    username.setText("");
                    password.setText("");
                    email.setText("");
                    phone.setText("");

                    websitename.setEditable(true);
                    username.setEditable(true);
                    password.setEditable(true);
                    email.setEditable(true);
                    phone.setEditable(true);
                } else {
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
                    websitename.setText(parent.getUserObject().toString());

                    String selectedUserName = selectedNode.getUserObject().toString();
                    for (Website website : currentUser.getWebsites()) {
                        if (website.getName().equals(parent.getUserObject().toString())) {
                            for (Account account : website.getAccounts()) {
                                if (account.getUsername().equals(selectedUserName)) {
                                    username.setText(account.getUsername());
                                    password.setText(account.getPassword());
                                    email.setText(account.getEmail());
                                    phone.setText(account.getPhone());

                                    websitename.setEditable(true);
                                    username.setEditable(true);
                                    password.setEditable(true);
                                    email.setEditable(true);
                                    phone.setEditable(true);

                                    return;
                                }
                            }
                        }
                    }
                }
            }
        });

        addTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) websites.getLastSelectedPathComponent();
                if (selectedNode != null && selectedNode.getParent() == root) {
                    websitename.setText(selectedNode.getUserObject().toString());
                } else {
                    websitename.setText("");
                }

                username.setText("");
                password.setText("");
                email.setText("");
                phone.setText("");

                websitename.setEditable(true);
                username.setEditable(true);
                password.setEditable(true);
                email.setEditable(true);
                phone.setEditable(true);
            }
        });

//        editButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) websites.getLastSelectedPathComponent();
//                if (selectedNode != null) {
//                    websitename.setEditable(true);
//                    username.setEditable(true);
//                    password.setEditable(true);
//                    email.setEditable(true);
//                    phone.setEditable(true);
//                } else {
//                    JOptionPane.showMessageDialog(dashboardFrame, "Please select a website or an account to edit.", "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });

        saveAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String savedWebsiteName = websitename.getText().trim();
                String savedUsername = username.getText().trim();
                String savedPassword = password.getText().trim();
                String savedEmail = email.getText().trim();
                String savedPhone = phone.getText().trim();

                // website, username and password is a must, email and phone are optional.
                if (savedUsername.isEmpty() || savedPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(dashboardFrame, "Please fill in username and password", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (savedWebsiteName.isEmpty()) {
                    JOptionPane.showMessageDialog(dashboardFrame, "Please fill in website name", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // get website list from user -> public List<Website> getWebsites()
                Website websiteNode = null;
                for (Website website : currentUser.getWebsites()) {
                    if (website.getName().equals(savedWebsiteName)) {
                        websiteNode = website;
                        break;
                    }
                }

                if (websiteNode == null) {
                    websiteNode = new Website(savedWebsiteName);
                    currentUser.addWebsite(websiteNode);

                    DefaultMutableTreeNode root = (DefaultMutableTreeNode) websites.getModel().getRoot();
                    DefaultMutableTreeNode newWebsiteNode = new DefaultMutableTreeNode(savedWebsiteName);
                    root.add(newWebsiteNode);
                }

                boolean isAccountExist = false;
                for (Account account : websiteNode.getAccounts()) {
                    if (account.getUsername().equals(savedUsername)) {
                        JOptionPane.showMessageDialog(dashboardFrame, "Account already exists, you are modifying your account");
                        account.setPassword(savedPassword);
                        account.setEmail(savedEmail);
                        account.setPhone(savedPhone);
                        isAccountExist = true;
                        JOptionPane.showMessageDialog(dashboardFrame, "Account successfully updated");
                        break;
                    }
                }

                if (!isAccountExist) {
                    Account newAccount = new Account(websiteNode, savedUsername, savedEmail, savedPhone, savedPassword);
                    websiteNode.addAccount(newAccount);

                    DefaultMutableTreeNode websiteTreeNode = findWebsiteNode(savedWebsiteName);
                    if (websiteTreeNode != null) {
                        websiteTreeNode.add(new DefaultMutableTreeNode(savedUsername));
                    }
                }

                ((DefaultTreeModel) websites.getModel()).reload();
                FileUtil.fileUtil.writeUserInfo(currentUser);

                JOptionPane.showMessageDialog(dashboardFrame, "Account saved!");
            }
            private DefaultMutableTreeNode findWebsiteNode(String websiteName) {
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) websites.getModel().getRoot();
                Enumeration<TreeNode> children = root.children();
                while (children.hasMoreElements()) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
                    if (node.getUserObject().toString().equals(websiteName)) {
                        return node;
                    }
                }
                return null;
            }
        });

        deleteTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) websites.getLastSelectedPathComponent();
                if (selectedNode == null || selectedNode.isRoot()) {
                    JOptionPane.showMessageDialog(dashboardFrame, "Please select a website or an account to delete",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
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
                    FileUtil.fileUtil.writeUserInfo(currentUser);

                    JOptionPane.showMessageDialog(dashboardFrame, "Account deleted!");
                }
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

//    public static void main(String[] args) {
//        new PasswordManagerGUI();
//    }

}