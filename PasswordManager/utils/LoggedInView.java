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


public class LoggedInView extends JFrame implements ListSelectionListener {
    private JList<String> accountList;
    private JPanel accountInfoPanel;
    private JList <String> accountsList;
    private JScrollPane scrollPane;
    private JSplitPane splitPane;
    private JButton editAccountButton;
    private JButton saveAccountButton;
    private JButton deleteAccountButton;
    private JButton addAccountButton;
    private JLabel accountUserNameLabel;
    private JLabel accountPasswordLabel;
    private JTextField accountUserNameTextField;
    private JTextField accountPasswordTextField;
    private String[] userWedsiteNames = {"Facebook", "Twitter", "Instagram", "Google"};

    public LoggedInView() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Your Account");

        // To set the size of frame
        this.setSize(800, 400);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        accountInfoPanel = new JPanel();
        accountInfoPanel.setLayout(new GridLayout(3, 2));

        accountUserNameLabel = new JLabel("User Name");
        accountPasswordLabel = new JLabel("Password");
        accountUserNameTextField = new JTextField();
        accountUserNameTextField.setSize(50, 20);
        accountPasswordTextField = new JTextField();
        editAccountButton = new JButton("Edit Account");
        saveAccountButton = new JButton("Save Account");


        accountInfoPanel.add(accountUserNameLabel);
        accountInfoPanel.add(accountUserNameTextField);
        accountInfoPanel.add(accountPasswordLabel);
        accountInfoPanel.add(accountPasswordTextField);
        accountInfoPanel.add(editAccountButton);
        accountInfoPanel.add(saveAccountButton);


        accountsList = new JList<>(userWedsiteNames);
        accountsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountsList.addListSelectionListener(this);
        scrollPane = new JScrollPane(accountsList);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, accountInfoPanel);
        splitPane.setDividerLocation(200);

        add(splitPane);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoggedInView();
            }
        });
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            // Only handle final selection change
            // Get selected account name
            String selectedAccount = accountsList.getSelectedValue();            
            if (selectedAccount != null) {
                
                accountUserNameTextField.setText(selectedAccount + "_user");
                // Simulated username
                accountPasswordTextField.setText("password123"); // Simulated password
            }
        }
    }
}
