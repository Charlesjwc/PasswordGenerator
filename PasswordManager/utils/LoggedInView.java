package utils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class LoggedInView extends JFrame implements ListSelectionListener, ActionListener, MouseListener {


    private JList<String> accountList;
    private JPanel accountInfoPanel;
    private JList <String> accountsList;
    private JScrollPane scrollPane;
    private JSplitPane splitPane;
    private JButton editAccountButton;
    private JButton saveAccountButton;
    private JButton generatePasswordButton;
    private JButton deleteAccountButton;
    private JButton addAccountButton;
    private JLabel accountUserNameLabel;
    private JLabel accountPasswordLabel;
    private JTextField accountUserNameTextField;
    private JTextField accountPasswordTextField;
    private String[] userWedsiteNames = {"Facebook", "Twitter", "Instagram", "Google"};
    private JMenuBar menuBar;
    private JMenu fillerMenu = new JMenu("");
    private JButton signOutButton;;
    public LoggedInView() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Your Account");

        // To set the size of frame
        this.setSize(800, 400);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        accountInfoPanel = new JPanel();
        accountInfoPanel.setLayout(new GridLayout(7, 2));

        accountUserNameLabel = new JLabel("User Name");
        accountPasswordLabel = new JLabel("Password");
        accountUserNameTextField = new JTextField();
        accountUserNameTextField.setSize(50, 20);
        accountPasswordTextField = new JTextField();
        editAccountButton = new JButton("Edit Account");
        saveAccountButton = new JButton("Save Account");
        generatePasswordButton = new JButton("Generate New Password");
        menuBar = new JMenuBar();
        addAccountButton = new JButton("Add Account");
        deleteAccountButton = new JButton("Delete Account");
        signOutButton = new JButton("Sign Out");

        menuBar.add(addAccountButton);
        menuBar.add(deleteAccountButton);
        this.setJMenuBar(menuBar);
        fillerMenu.setEnabled(false); // Disable the filler menu so it doesn't appear in the menu
        fillerMenu.add(new JMenuItem(" ")); // Add an empty item for spacing
        fillerMenu.setPreferredSize(new Dimension(400, 30)); // Give it some width to push the next items
        menuBar.add(fillerMenu);
        menuBar.add(signOutButton);

        // Add the filler menu to the menu bar

        accountInfoPanel.add(accountUserNameLabel);
        accountInfoPanel.add(accountUserNameTextField);
        accountInfoPanel.add(accountPasswordLabel);
        accountInfoPanel.add(accountPasswordTextField);
        accountInfoPanel.add(generatePasswordButton);
        generatePasswordButton.setEnabled(false);
        accountInfoPanel.add(editAccountButton);
        accountInfoPanel.add(saveAccountButton);
        saveAccountButton.setVisible(false);
        editAccountButton.addActionListener(this);
        saveAccountButton.addActionListener(this);
        generatePasswordButton.addActionListener(this);
        accountUserNameTextField.addMouseListener(this);
        accountPasswordTextField.addMouseListener(this);

        accountsList = new JList<>(userWedsiteNames);
        accountsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountsList.addListSelectionListener(this);
        scrollPane = new JScrollPane(accountsList);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, accountInfoPanel);
        splitPane.setDividerLocation(200);

        add(splitPane);

        setVisible(true);
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoggedInView();
            }
        });
    }*/
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == editAccountButton) {
            accountUserNameTextField.setEnabled(true);
            accountPasswordTextField.setEnabled(true);
            generatePasswordButton.setEnabled(true);
            editAccountButton.setVisible(false);
            saveAccountButton.setVisible(true);

        }
        if (e.getSource() == generatePasswordButton){
            Generator generator = new Generator();

            accountPasswordTextField.setText(generator.autoGenerator());

        }
        if (e.getSource() == saveAccountButton){

        }
    }
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            // Only handle final selection change
            // Get selected account name
            String selectedAccount = accountsList.getSelectedValue();            if (selectedAccount != null) {
                // Dummy data for account info based on the selected account
                // You can replace this with actual account data from a database or file
                accountUserNameTextField.setText(selectedAccount + "_user");
                accountUserNameTextField.setEnabled(false);
                // Simulated username
                accountPasswordTextField.setText("password123"); // Simulated password
                accountPasswordTextField.setEnabled(false);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == accountUserNameTextField) {
            String username = accountUserNameTextField.getText();
            StringSelection selection = new StringSelection(username);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            JOptionPane.showMessageDialog(LoggedInView.this, "Username copied to clipboard: " + username);
        }
        if (e.getSource() == accountPasswordTextField) {
            String password = accountPasswordTextField.getText();
            StringSelection selection = new StringSelection(password);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            JOptionPane.showMessageDialog(LoggedInView.this, "Password copied to clipboard: " + password);

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
}




