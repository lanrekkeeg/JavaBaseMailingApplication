package pkg;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class ViewAndReply extends JFrame {

	private JPanel contentPane;
	private ViewAndReply frame;
	private JLabel label;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewAndReply frame = new ViewAndReply();
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
	public ViewAndReply() {
	}
	public ViewAndReply(String Message, View v,int index,String check) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 693, 486);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		frame = this;
		JButton btnReply = new JButton("Reply");
		btnReply.setBounds(83, 381, 97, 25);
		
		label = new JLabel("");
		label.setBounds(523, 138, 238, 25);
		contentPane.add(label);
		
		JButton btnDownload = new JButton("Download");
		btnDownload.setBounds(523, 176, 97, 25);
		contentPane.add(btnDownload);
		btnDownload.setVisible(false);
		btnReply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// we sill send message over here
				frame.dispose();
				v.setVisible(true);
			}
		});
		if(index != -1) {
			// extract the file name and wait for the download
			String str = file_name(index,check);
			btnDownload.setVisible(true);
			btnDownload.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// call the download where file will be download
					System.out.println("File name is "+str);
					Download_And_Save_File(index,str,check);
				}
			});
		}
		contentPane.add(btnReply);
		//btnReply.addActionListener(arg0);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(64, 23, 447, 178);
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setText(Message);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(64, 249, 455, 123);
		contentPane.add(scrollPane_1);
		
		JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		
		
		
	}
	public String file_name(int serial,String check) {
		
		String Filename = null;
		
		try{
		Socket sc = new Socket("localhost",49159);
		System.out.println("Download and Save Function");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
		PrintWriter out = new PrintWriter(sc.getOutputStream(),true);
		out.println("1"+"\n"+"FileName"+"\n"+serial+"\n"+check); // check the other end
		Filename = in.readLine();
		label.setText(Filename);
		}catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Filename;
		
	}
	public void Download_And_Save_File(int serial,String Filename,String check){
		
		//Create the Socket 
		// Write the "Read File"
		// create new socket
		// Extract the file name
		try {
			Socket sc = new Socket("localhost",49159);
			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			PrintWriter out = new PrintWriter(sc.getOutputStream(),true);
			
			String current_directory = System.getProperty("user.dir");
			current_directory += "\\"+Filename;
			// Now create the File
			File download_file = new File(current_directory);
			// define mechanism for duplicate file
			download_file.createNewFile();
			// here we make the changes
			out.println("1\n"+"BLOB_DATA"+"\n"+serial+"\n"+check);
			
			Socket file_reader = new Socket("localhost",45914);
			InputStream cin = file_reader.getInputStream();
			BufferedOutputStream cout = new BufferedOutputStream(new FileOutputStream(download_file));
			byte[] file_data = new byte[200];
			int current = 0;
			while((current = cin.read(file_data))>0) {
				cout.write(file_data, 0, current);
				
			}
			cout.flush();
			cout.close();
			file_reader.close();
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
