package pkg;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;

public class CreateAccount extends JFrame {

	private JPanel contentPane;
	private JTextField n_email;
	private JTextField n_password;
	private JTextField n_confirm;
	private static CreateAccount frame;
	private Statement st;
	private Connection con;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new CreateAccount();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
public static boolean isValidMail(String email) {
		
		int atCount = 0;
		boolean withinQuotes = false;
		
		for(int i = 0; i < email.length(); i++) {
			char x = email.charAt(i);
			if(x == '\"') {
				withinQuotes = !withinQuotes;
				continue;
			}
			if(!((x == '!') || (x >= '#' && x <= '\'' ) || (x == '*') || (x == '+') || (x >= '-' && x <= '9') ||
					(x == '?') || (x >= '?' && x<= 'Z') || (x >= '^' && x <= '~'))){
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

	void create_Account(UserLogin l, CreateAccount A) {
		try {
			
			String username = n_email.getText();
			String pass = n_password.getText();
			String confirm = n_confirm.getText();
			String Response = null;
			//System.out.println("Sending Request to server:"+pass+":"+confirm);
			if(pass.equals(confirm)) {
				
				Socket UserDemand = new Socket("localhost",49159);
				BufferedReader in = new BufferedReader(new InputStreamReader(UserDemand.getInputStream()));
				PrintWriter out = new PrintWriter(UserDemand.getOutputStream(),true);
				out.println("1\n"+"CreateAccount\n"+username+"\n"+pass);
				
				while((Response = in.readLine())!= null) {
				
				if(Response.equals("Non-Negative")) {
					JOptionPane.showConfirmDialog(contentPane, "User Already Exist");
					l.setVisible(true);
					A.dispose();
					UserDemand.close();
					break;
				}
				if(Response.equals("Negative")) {
					JOptionPane.showConfirmDialog(contentPane, "User Already Exist");
				}}
		}} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}
	public CreateAccount() {}
	// overloaded constructor
	public CreateAccount(UserLogin f) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 772, 568);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblWelcomeToFmail = new JLabel("Welcome to Fmail");
		lblWelcomeToFmail.setBounds(303, 49, 138, 16);
		contentPane.add(lblWelcomeToFmail);
		
		JLabel lblRegistrationForm = new JLabel("Registration Form");
		lblRegistrationForm.setBounds(73, 101, 123, 16);
		contentPane.add(lblRegistrationForm);
		
		JLabel lblEmailId = new JLabel("Email ID");
		lblEmailId.setBounds(73, 160, 56, 16);
		contentPane.add(lblEmailId);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(73, 199, 72, 16);
		contentPane.add(lblPassword);
		
		JLabel lblConfirmPassword = new JLabel("confirm Password");
		lblConfirmPassword.setBounds(73, 228, 114, 16);
		contentPane.add(lblConfirmPassword);
		
		n_email = new JTextField();
		n_email.setBounds(209, 157, 152, 22);
		contentPane.add(n_email);
		n_email.setColumns(10);
		
		n_password = new JTextField();
		n_password.setBounds(209, 196, 152, 22);
		contentPane.add(n_password);
		n_password.setColumns(10);
		
		n_confirm = new JTextField();
		n_confirm.setBounds(209, 225, 152, 22);
		contentPane.add(n_confirm);
		n_confirm.setColumns(10);
		frame = this;
		JButton btnNewButton = new JButton("Register");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				// Ahmad awan code in the Action
				if(isValidMail(n_email.getText()))
				create_Account(f,frame);
				else
					JOptionPane.showConfirmDialog(contentPane, "Invalid Email ID");
				
			}
		});
		btnNewButton.setBounds(73, 319, 123, 25);
		contentPane.add(btnNewButton);
		
	}

}
