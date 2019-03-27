package pkg;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class Setting extends JFrame {

	private JPanel contentPane;
	private JTextField old_p;
	private JTextField new_p;
	private JTextField confirm_p;
	private Setting st;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Setting frame = new Setting();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public void set_password(String user) {
		
		String old_password = old_p.getText();
		String new_Password = new_p.getText();
		String confirm_password = confirm_p.getText();
		String res = " ";
		Socket sc;
		try {
			sc = new Socket("localhost",49159);
			BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			PrintWriter out = new PrintWriter(sc.getOutputStream(),true);
			out.println("2\n"+"verify\n"+old_password);
				res = in.readLine();
				
				if(res.equals("confirm")) {
					if(new_Password.equals(confirm_password)) {
						
						out.println("Update_P\n"+user+"\n"+new_Password);
						sc.close();
					}
				}	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Setting() {
		}
	public Setting(View v_1,String user) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(150, 150, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		getContentPane().setLayout(null);
		
		JLabel lblOldPassword = new JLabel("old Password");
		lblOldPassword.setBounds(87, 92, 98, 16);
		getContentPane().add(lblOldPassword);
		
		JLabel lblNewPassword = new JLabel("New Password");
		lblNewPassword.setBounds(87, 138, 98, 16);
		getContentPane().add(lblNewPassword);
		
		JLabel lblConfirmPassword = new JLabel("Confirm Password");
		lblConfirmPassword.setBounds(87, 191, 111, 16);
		getContentPane().add(lblConfirmPassword);
		
		old_p = new JTextField();
		old_p.setBounds(217, 89, 147, 22);
		getContentPane().add(old_p);
		old_p.setColumns(10);
		
		new_p = new JTextField();
		new_p.setBounds(217, 135, 147, 22);
		getContentPane().add(new_p);
		new_p.setColumns(10);
		
		confirm_p = new JTextField();
		confirm_p.setBounds(217, 188, 147, 22);
		getContentPane().add(confirm_p);
		confirm_p.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		st = this;
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				set_password(user);
				st.dispose();
				v_1.setVisible(true);
				
			}
		});
		btnSave.setBounds(203, 289, 97, 25);
		getContentPane().add(btnSave);
	}

}
