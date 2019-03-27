package pkg;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;
public class UserLogin extends JFrame {

	private JPanel contentPane;
	private JTextField email_id;
	private JPasswordField password;
	private static UserLogin frame_;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			 	try {
					frame_ = new UserLogin();
					frame_.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
public static boolean isValidMail(String email) {
		
		int atCount = 0;
		boolean withinQuotes = false;
		
		for(int i = 0; i < email.length(); i++) {
			char x = email.charAt(i);
			if(x == '\"') {
				withinQuotes = !withinQuotes;
				continue;
			}
			if(!((x == '!') || (x >= '#' && x <= '\'' ) || (x == '*') || (x == '+') || (x >= '-' && x <= '9')
				||(x == '?') || (x >= '?' && x<= 'Z') || (x >= '^' && x <= '~'))){
				if(!withinQuotes)
					return false;
			}
			
			if(!withinQuotes && x == '.' && email.charAt(i + 1) == '.')
				return false;
			
			if(x == '@')
				atCount++;
		}
		
		if(withinQuotes)
			return false;

		email = email.toLowerCase();
		
		if(atCount != 1)
			return false;
		
		String postfix = email.substring(email.lastIndexOf('@') + 1, email.length());
		String prefix = email.substring(0,email.lastIndexOf('@'));
		
		if(!postfix.contentEquals("colab.com") || prefix.charAt(0) == '.' || prefix.charAt(prefix.length() - 1) == '.')
			return false;
		
		return true;
	}

	void login() {
		try {
		
			Socket UserDemand = new Socket("localhost",49159);
			BufferedReader in  = new BufferedReader(new InputStreamReader(UserDemand.getInputStream()));
			PrintWriter out = new PrintWriter(UserDemand.getOutputStream(),true);
			out.println("1\n"+"Login\n"+email_id.getText()+"\n"+password.getText());
			String str = null;
			str = in.readLine();
		
			if(str.equals("1")) {
				
				View V = new View(frame_ ,email_id.getText(),password.getText()); // incase of sign out
				V.setVisible(true);
				frame_.dispose();
				UserDemand.close();
				//break;
				}
				if(str.equals("0")) {
					JOptionPane.showMessageDialog(contentPane, "Invalid  username OR password\n");
					return;
				}
			System.out.println("hi");
		}
		catch(IOException e) {
			// print the message on the jpanel;
			JOptionPane.showMessageDialog(contentPane, "server is busy\n");
		}
	}
	public UserLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 779, 476);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// Ahmad Awan code in action
				if(isValidMail(email_id.getText()))
				login();
				else
				JOptionPane.showConfirmDialog(contentPane, "Invalid Email ID");
			}
		});
		btnNewButton.setForeground(Color.DARK_GRAY);
		btnNewButton.setBounds(282, 293, 144, 25);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Sign Up");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CreateAccount CA = new CreateAccount(frame_);
				frame_.dispose();
				CA.setVisible(true);
			}
		});
		btnNewButton_1.setBounds(282, 346, 144, 25);
		contentPane.add(btnNewButton_1);
		
		email_id = new JTextField();
		email_id.setBounds(277, 182, 149, 22);
		contentPane.add(email_id);
		email_id.setColumns(10);
		
		password =  new JPasswordField();
		password.setBounds(277, 238, 149, 22);
		contentPane.add(password);
		password.setColumns(10);
		
		JLabel lblEmailId = new JLabel("Email ID");
		lblEmailId.setBounds(168, 185, 66, 16);
		contentPane.add(lblEmailId);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(168, 241, 56, 16);
		contentPane.add(lblPassword);
	
	}
}
