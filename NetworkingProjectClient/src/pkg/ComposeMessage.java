package pkg;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class ComposeMessage extends JFrame {

	private JPanel contentPane;
	private JTextField EmailF;
	private JTextField SubjectF;
	private ComposeMessage comp;
	private String fileName;
	private String Directory;
	JTextArea textArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ComposeMessage frame = new ComposeMessage();
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
	public void send_mail(String user,String directory,String file) {
		
		  try {
			Socket u = new Socket("localhost",49159);
			BufferedReader in = new BufferedReader(new InputStreamReader(u.getInputStream()));
			PrintWriter out = new PrintWriter(u.getOutputStream(),true);
			out.println("1\n"+"MailDelivery\n"+user+"\n"+EmailF.getText()+"\n"+SubjectF.getText()+"\n"+textArea.getText());
			if(!(file.isEmpty())) {
				out.println("Attachment");
				String[] split_name = file.split("[.]");
				String first_portion = "";
				for(int i =0;i<split_name.length-1;i++)
					first_portion += split_name[i];
				out.println(first_portion+"\n"+split_name[split_name.length-1]);
				
				Socket f = new Socket("localhost",5002);
				File name = new File(directory);
				OutputStream o = f.getOutputStream();
				byte [] bytearray  = new byte [(int)name.length()]; // allocate the number of bytes
				BufferedInputStream f_in = new BufferedInputStream(new FileInputStream(name));
				f_in.read(bytearray,0,bytearray.length);
				o.write(bytearray, 0, bytearray.length);
				o.flush();
				f.close();
				return;
			}
			out.println("NoAttachment");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}}
	public ComposeMessage() {
		
		comp = this;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 619, 497);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblEmailid = new JLabel("EmailID");
		lblEmailid.setBounds(58, 68, 56, 16);
		contentPane.add(lblEmailid);
		
		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setBounds(58, 112, 56, 16);
		contentPane.add(lblSubject);
		
		JLabel lblMessage = new JLabel("Message");
		lblMessage.setBounds(58, 151, 56, 16);
		contentPane.add(lblMessage);
		
		EmailF = new JTextField();
		EmailF.setBounds(114, 65, 164, 22);
		contentPane.add(EmailF);
		EmailF.setColumns(10);
		
		SubjectF = new JTextField();
		SubjectF.setBounds(114, 109, 164, 22);
		SubjectF.setText("");
		contentPane.add(SubjectF);
		SubjectF.setColumns(10);
	
		
		JButton btnAddattachement = new JButton("Addattachement");
		btnAddattachement.setBounds(337, 411, 135, 25);
		btnAddattachement.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				 JFileChooser c = new JFileChooser();
			}});
		contentPane.add(btnAddattachement);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(112, 151, 186, 160);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(480, 411, 121, 25);
		contentPane.add(lblNewLabel);
	}
	public ComposeMessage(String user, View _view) {
		comp = this;
		fileName =  "";
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 578, 496);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel filename = new JLabel("");
		JLabel lblEmailid = new JLabel("EmailID");
		lblEmailid.setBounds(58, 68, 56, 16);
		contentPane.add(lblEmailid);
		
		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setBounds(58, 112, 56, 16);
		contentPane.add(lblSubject);
		
		JLabel lblMessage = new JLabel("Message");
		lblMessage.setBounds(58, 151, 56, 16);
		contentPane.add(lblMessage);
		
		EmailF = new JTextField();
		EmailF.setBounds(114, 65, 164, 22);
		contentPane.add(EmailF);
		EmailF.setColumns(10);
		
		SubjectF = new JTextField();
		SubjectF.setText("");
		SubjectF.setBounds(114, 109, 164, 22);
		contentPane.add(SubjectF);
		SubjectF.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			send_mail(user,Directory,fileName);
			comp.dispose();
			_view.setVisible(true);
		}});
		btnSend.setBounds(126, 411, 127, 25);
		contentPane.add(btnSend);
		
		JButton btnAddattachement = new JButton("Addattachement");
		btnAddattachement.setBounds(337, 411, 135, 25);
		btnAddattachement.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				 JFileChooser c = new JFileChooser();
			      // Demonstrate "Open" dialog:
			      int rVal = c.showOpenDialog(ComposeMessage.this);
			      if (rVal == JFileChooser.APPROVE_OPTION) {
			        filename.setText(c.getSelectedFile().getName());
			        fileName = c.getSelectedFile().getName();
			        Directory = c.getCurrentDirectory().toString()+"\\"+fileName;
			        
			      }
			      if (rVal == JFileChooser.CANCEL_OPTION) {
			        filename.setText("You pressed cancel");
			      }
			}});
		contentPane.add(btnAddattachement);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(112, 151, 186, 160);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		filename.setBounds(480, 411, 121, 25);
		contentPane.add(filename);
	}
}
