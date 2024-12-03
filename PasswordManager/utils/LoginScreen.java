package utils;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;


public class LoginScreen implements ActionListener{

	//create new account btn
	
	private JLabel userLabel;
	private JLabel passwordLabel;
	private JPanel panel;
	private JFrame frame;
	private JTextField userText;
	private JPasswordField passwordText;
	private JButton loginButton;
	private JButton createNewAccountButton;
	private List <User> userList;
	private static final String USERNAME = "user";
	private static final String PASSWORD = "password";

	
	public LoginScreen(List <User> userList) {
		this.userList = userList;
		frame = new JFrame();
		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(3, 2, 10, 10));
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Password Manager");
		frame.pack();
		frame.setVisible(true);
		frame.setSize(400, 230);
		
		userLabel = new JLabel("Username: ");  
	    panel.add(userLabel);

	       
	    userText = new JTextField(20);
	    panel.add(userText);

	      
	    passwordLabel = new JLabel("Password: ");
	    panel.add(passwordLabel);

	       
	    passwordText = new JPasswordField(20);
	    panel.add(passwordText);

	    // Creating login button
	    loginButton = new JButton("login");
	    panel.add(loginButton);
	    
	    //creating create new account btn
	    createNewAccountButton = new JButton("Create Account");
		panel.add(createNewAccountButton);
		
		loginButton.addActionListener(this);
		createNewAccountButton.addActionListener(this);
	}
public boolean checkUser(List<User> userList){
		String userName = userText.getText();
		String password = new String(passwordText.getPassword());
		for(User user : userList){
			if(user.getUsername().equals(userName) && user.getPassword().equals(password)){
				return true;
			}
		}

		return false;

}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			String username = userText.getText();
			char[] password = passwordText.getPassword();
			
			if (checkUser(userList)) {
				JOptionPane.showMessageDialog(frame, "Login successful!");
				//set userpassword


                new LoggedInView();
                frame.dispose();

			}else {
				JOptionPane.showMessageDialog(frame, "Login unsuccessful. Please check credentials or create an accoount.");
			}
		}else if (e.getSource() == createNewAccountButton) {
			if (userText.getText().equals(USERNAME)) {
				JOptionPane.showMessageDialog(frame, "Username already exists, please enter proper password, or a new unique username.");

			}else if (userText.getText() != null && passwordText.getPassword() != null){
                User newUser = null;
                try {
                    newUser = new User(userText.getText(), passwordText.hashCode(), passwordText.getPassword().toString());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                FileUtil.fileUtil.writeUserInfo(newUser);
				JOptionPane.showMessageDialog(frame, "User Successfully Created.");

			}else{
				JOptionPane.showMessageDialog(frame, "An error occurred, please try again.");

			}
		}

	}

}
	
