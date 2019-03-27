package pkg;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.ListModel;

import java.awt.List;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import java.awt.SystemColor;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
public class View extends JFrame {

	private JPanel contentPane;
	private static View frame_2;
	private JTable table;
	private String user;
	private String pass;
	private ArrayList<String> listMessage;
	private Map<Integer,Integer> serial = new HashMap<Integer,Integer>();
	private Map<Integer,Integer> serial_all = new HashMap<Integer,Integer>();
	private String which_table;
	private boolean delete;
	private JTable table_1;
	private DefaultTableModel model;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 frame_2 = new View();
					frame_2.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 */
	
	public void delete_message(int index,String check) {
		// for loop to iterate the indices
		// retrieve the serial no
		// send it to the delete message query
		try {
			Socket sc = new Socket("localhost",49159);
			BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			PrintWriter out = new PrintWriter(sc.getOutputStream(),true);
			//for(int i = 0;i<indices.length;i++) {
				
				int serial = check_Attachment(index,serial_all);
				System.out.println("Serial Number to be deleted "+serial);
				out.println("1\n"+"Delete_Message\n"+check+"\n"+serial);
				InboxReading(check);
			//}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void InboxReading(String  check) {
		try {
			
			ArrayList<String> listSubject = new ArrayList<String>();
			listMessage = new ArrayList<String>();
			String ser;
			String Sender = null;
			String subject;
			String Message;
			int ser_values = 0;
			serial.clear();
			listSubject.clear();
			Socket sc = new Socket("localhost",49159);
			BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			PrintWriter out = new PrintWriter(sc.getOutputStream(),true);
			out.println("1\n"+"ExtractEmail"+"\n"+user+"\n"+check);
			int record_count = 0;
			DefaultTableModel dtm = (DefaultTableModel) table_1.getModel();
			dtm.setRowCount(0);
			do {
				
				// i make changes in ser if condtion
				record_count++;
				ser = in.readLine();
				
				if(ser.equals("NULL_Terminated")) {
					break;
				}
				ser_values = Integer.parseInt(ser);
				if(!(ser.equals("NULL"))) {
				
					ser_values = Integer.parseInt(ser);
				}
				
				String file = in.readLine();
				Sender = in.readLine();
				if(!(file.equals("nullnull")))
					serial.put(record_count,ser_values);
				
			// we need this here because in the next line it will wait for another input
				subject = in.readLine();

				if(file.equals("nullnull")){
					file = " ";
						}
				
				Object [] row = {Sender,subject,file};
				model.addRow(row);
				Message = in.readLine();
				listMessage.add(Message);
				serial_all.put(record_count, ser_values);
				
			}while(true);
			String str = in.readLine();
			
			if(str.equals("Now_Attachment")) {
				do {
					str = in.readLine();
					if(str.equals("NULL"))
						break;
					
				}
				while(true);
			}
			
			sc.close();
			System.out.println("Counter is "+record_count);
			
		} catch (UnknownHostException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setting(String user) {
		
		
	}
	public View() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400,400, 600, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		this.user = user;
		this.pass = pass;
		delete = false;
		getContentPane().setLayout(null);
		InboxReading("NO");
		contentPane.setLayout(null);
		JLabel lblWelcomeToFastmail = new JLabel("Welcome To FastMail");
		lblWelcomeToFastmail.setBounds(223, 39, 134, 16);
		getContentPane().add(lblWelcomeToFastmail);
		
		JButton btnComposeMessage = new JButton("Compose Message");
		btnComposeMessage.setBounds(0, 94, 168, 25);
		frame_2 = this;
		btnComposeMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ComposeMessage M = new ComposeMessage(user,frame_2);
				frame_2.dispose();
				M.setVisible(true);
			}
		});
		
		btnComposeMessage.setForeground(new Color(147, 112, 219));
		getContentPane().add(btnComposeMessage);
		JButton btnI = new JButton("Inbox");
		btnI.setBounds(12, 145, 97, 25);
		btnI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				which_table = "receiver";
				delete = false;
				InboxReading("NO");
			}
		});
		contentPane.add(btnI);
		
		JButton btnSent = new JButton("Sent");
		btnSent.setBounds(12, 184, 97, 25);
		getContentPane().add(btnSent);
		btnSent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent v) {
				which_table = "sender";
				delete = false;
				InboxReading("YES");
			}
		});
		JButton btnDeleteMessage = new JButton("Delete Message");
		btnDeleteMessage.setBounds(12, 234, 123, 25);
		btnDeleteMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				delete = true;
			}
		});
		getContentPane().add(btnDeleteMessage);
		table = new JTable();
		table.setBounds(240, 169, 1, 1);
		getContentPane().add(table);
		
		JButton btnSignOut = new JButton("Sign out");
		btnSignOut.setBounds(473, 13, 97, 25);
		contentPane.add(btnSignOut);
		btnSignOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argo) {
				
			}
			
		});
		JButton btnSetting = new JButton("Setting");
		btnSetting.setBounds(371, 13, 97, 25);
		btnSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Setting st = new Setting(frame_2,user);
				frame_2.dispose();
				st.setVisible(true);
			}
		});
		contentPane.add(btnSetting);
		JScrollPane scrollPane = new JScrollPane();
		
		scrollPane.setBounds(223, 94, 283, 193);
		contentPane.add(scrollPane);
		
		table_1 = new JTable();
		model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Sender", "Subject", "Attachment"
				}
			);
		table_1.setModel(model);
		scrollPane.setViewportView(table_1);
		
	}
	public int check_Attachment(int index,Map<Integer,Integer> ser) {
		for(Map.Entry<Integer, Integer> entry :ser.entrySet()) {
			if(entry.getKey() == index)
				return entry.getValue();
			
		}
		return -1;
	}
	public View(UserLogin G, String user, String pass) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400,400, 600, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		this.user = user;
		this.pass = pass;
		delete = false;
		getContentPane().setLayout(null);
		
		contentPane.setLayout(null);
		JLabel lblWelcomeToFastmail = new JLabel("Welcome To FastMail");
		lblWelcomeToFastmail.setBounds(223, 39, 134, 16);
		getContentPane().add(lblWelcomeToFastmail);
		
		JButton btnComposeMessage = new JButton("Compose Message");
		btnComposeMessage.setBounds(0, 94, 168, 25);
		frame_2 = this;
		btnComposeMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ComposeMessage M = new ComposeMessage(user,frame_2);
				frame_2.dispose();
				M.setVisible(true);
			}
		});
		
		btnComposeMessage.setForeground(new Color(147, 112, 219));
		getContentPane().add(btnComposeMessage);
		JButton btnI = new JButton("Inbox");
		btnI.setBounds(12, 145, 97, 25);
		btnI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				which_table = "receiver";
				delete = false;
				InboxReading("NO");
			}
		});
		contentPane.add(btnI);
		JButton btnSent = new JButton("Sent");
		btnSent.setBounds(12, 184, 97, 25);
		getContentPane().add(btnSent);
		btnSent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent v) {
				which_table = "sender";
				delete = false;
				InboxReading("YES");
			}
		});
		JButton btnDeleteMessage = new JButton("Delete Message");
		btnDeleteMessage.setBounds(12, 234, 123, 25);
		btnDeleteMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				delete = true;
			}
		});
		getContentPane().add(btnDeleteMessage);
		/*table = new JTable();
		table.setBounds(240, 169, 1, 1);
		getContentPane().add(table);*/
		
		JButton btnSignOut = new JButton("Sign out");
		btnSignOut.setBounds(473, 13, 97, 25);
		contentPane.add(btnSignOut);
		btnSignOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argo) {
				G.setVisible(true);
				frame_2.dispose();
			}
			
		});
		JButton btnSetting = new JButton("Setting");
		btnSetting.setBounds(371, 13, 97, 25);
		btnSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Setting st = new Setting(frame_2,user);
				frame_2.dispose();
				st.setVisible(true);
			}
		});
		contentPane.add(btnSetting);
		/*JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(223, 94, 239, 193);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(list_1);*/
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(223, 94, 283, 193);
		contentPane.add(scrollPane);
		
		table_1 = new JTable();
		model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Sender", "Subject", "Attachment"
				}
			);
		table_1.setModel(model);
		scrollPane.setViewportView(table_1);
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
					    int row = table_1.rowAtPoint(arg0.getPoint());
					    
					    System.out.println("Row number is "+row);
					   // int []indices = list_1.getSelectedIndices();
						
						int check = check_Attachment(row+1,serial);
						System.out.println("check is "+check);
						if(delete == false) {
						ViewAndReply view_and_replace;
						view_and_replace = new ViewAndReply(listMessage.get(row),frame_2,check,which_table);
						view_and_replace.setVisible(true);
						}
						else 
							delete_message(row+1,which_table);
					 }
		});
	}
}
