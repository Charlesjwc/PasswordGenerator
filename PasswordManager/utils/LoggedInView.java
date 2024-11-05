package utils;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class LoggedInView extends JFrame {
	 private JList<String> accountList;
	 
	public LoggedInView(){
		 DefaultListModel<String> JlistModel = new DefaultListModel<>();	
		 JlistModel.addElement("Facebook");
	     JlistModel.addElement("Google");
	     JlistModel.addElement("Apple");
	accountList = new JList<>(JlistModel);
    add(accountList);
    
    accountList.addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                final List<String> selectedValuesList = accountList.getSelectedValuesList();
                System.out.println(selectedValuesList);
            }
        }
    });
    
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Your Accounts");
    
    // To set the size of frame      
    this.setSize(400,400);
    this.setVisible(true);
    this.setLocationRelativeTo(null);
    add(new JScrollPane(accountList));
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new LoggedInView();
        }
    });
}      
}
