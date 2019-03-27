import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.IntBuffer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
public class helper {

	private static Connection con;
	private static Statement sta;
	private static ResultSet res;
	private static PreparedStatement pr;
	public helper() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_data","root",""); 
			}
			catch(SQLException ex) {
				//JOptionPane.showMessageDialog(contentPane, "Connection error\n check username and password\nDetail:"+ex);
			}
			}
			catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
	}
	public static void Delete_Message(BufferedReader in,PrintWriter out) {
		// check whether to delete from recmail amd sendmail
		// delete it with for loop
		// delete recmail where serial_no = provide
		int serial = 0;
		String check = null;
		String qry = null;
		
		try {
			check = in.readLine();
			serial = Integer.parseInt(in.readLine());
			sta = con.createStatement();
			if(check.equals("receiver")) {
				qry = "Delete From recmail where serial_no = "+serial+";";
			}
			else
				qry = "Delete From sendmail where serial_no = "+serial+";";
			sta.execute(qry);
		} catch (NumberFormatException | IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void file_name(PrintWriter out,BufferedReader in)  {
		
		try {
			
			int serial = Integer.parseInt(in.readLine());
			String check = in.readLine();
			String qry = null;
			System.out.println("file_name check is "+check);
			if(check.equals("receiver"))
				qry = "select filename,type from recmail where serial_no = "+serial+";";
			else
				qry = "select filename,type from sendmail where serial_no = "+serial+";";
		
			sta = con.createStatement();
			res = sta.executeQuery(qry);
			if(res.next()) {
				out.println(res.getString(1)+"."+res.getString(2));
				System.out.println("File name is "+res.getString(1)+"."+res.getString(2));
				return;
			}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void Extracting_Blob_data(PrintWriter out,BufferedReader in) {
		
		try {
			
			int serial_no = Integer.parseInt(in.readLine());
			String check = in.readLine();
			ServerSocket server = new ServerSocket(45914);
			Socket client = server.accept();
			int byte_read = 0;
			byte[] chunk = new byte[200];
			BufferedOutputStream cout_byte = new BufferedOutputStream((client.getOutputStream()));
			InputStream cin = null;
			String qry = null;
			if(check.equals("receiver"))
				qry = "Select attachment from recmail where serial_no = "+serial_no+";";
			else qry = "Select attachment from sendmail where serial_no = "+serial_no+";";
			sta = con.createStatement();
			res = sta.executeQuery(qry);
			if(res.next()) {
				cin = res.getBinaryStream(1);
				
			}
			
			while((byte_read = cin.read(chunk))>0) {
				cout_byte.write(chunk, 0, byte_read);
			}
			cout_byte.flush();
			cout_byte.close();
		
			// here close all the buffer
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void InboxReader(String user,BufferedReader in,PrintWriter out) {
		try {
			String qry = null;
			String row = null;
			String check = in.readLine();
			sta = con.createStatement();
			int index = 2;
			// here check for recmail or sendmail
			
			if(check.equals("NO"))
				qry = "Select * from recmail where receiveremail ='"+user+"';";
			else {
				qry = "Select * from sendmail where  senderemail ='"+user+"';";
				index = 3;
			}
			res = sta.executeQuery(qry);
			
			ArrayList<String> file_name = new ArrayList<String>();
			ArrayList<Integer> serial_no = new ArrayList<Integer>();
			while(res.next()) {
				// here we need the check for receiver
				out.println(res.getString(1)+"\n"+res.getString(7)+res.getString(8)+"\n"+res.getString(index)+"\n"+res.getString(4)+"\n"+res.getString(5));
				if(res.getString(7) != null) {
					file_name.add(res.getString(7)+res.getString(8));
					serial_no.add(res.getInt(1));
				}
				
			}
			out.println("NULL_Terminated");
			out.println("Now_Attachment");
			for(int i = 0;i<file_name.size();i++)
				out.println(file_name.get(i)+"\n"+serial_no.get(i));
			out.println("NULL");
			
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static boolean CreateAccount(String email,String Password) {
		String qry = "insert into users values('"+email+"','"+Password+"');";
		String IFUser = "select EmailID from users where EmailID ='"+email+"';";
			try {
				sta = con.createStatement();
				res = sta.executeQuery(IFUser);
				if(res.next() == true) {
					
					 return true;
				}
				sta.execute(qry);
				qry = "insert into totalsr values('"+email+"',0,0);";
				sta.execute(qry);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return false;
		
	}
	public static boolean MailDelivery(PrintWriter out,BufferedReader in) {
		 // first we check whether user exist
		try {
			String user = in.readLine();
			String Rec = in.readLine();
			String Subject = in.readLine();
			String Message = in.readLine();
			System.out.println(Rec);
			String file_name;
			String file_type;
			String filename = "";
			boolean check = false;
			
			String qry = "Select EmailID  from users where EmailID = '"+Rec+"';";
			
			sta = con.createStatement();
			res = sta.executeQuery(qry);
			if(res.next() == false) {
				//Fix this query
				qry = "insert into recmail(senderemail,receiveremail,subject,message) values('MailServer','"+user+"','Mail Address Not Found','check the Receiver address.Provide Address is not found');";
				
				sta.execute(qry);
				
				 return false;
			}
			
			if(in.readLine().equals("Attachment")) {
				check = true;
				
				filename = in.readLine();
				file_name = filename;
				file_type = in.readLine();
				filename += "."+file_type;
			
				ServerSocket ser = new ServerSocket(5002);
				Socket sc = ser.accept();
				InputStream in_1 = sc.getInputStream();
				    qry ="insert into recmail(senderemail,receiveremail,subject,message,attachment,filename,type) values(?,?,?,?,?,?,?);";
	                
	                pr = con.prepareStatement(qry);
	                pr.setString(1,user);
	                pr.setString(2, Rec);
	                pr.setString(3,Subject);
	                pr.setString(4, Message);
	                pr.setBinaryStream(5, in_1);
	                pr.setString(6,file_name);
	                pr.setString(7, file_type);
	    			pr.executeUpdate();
	    			
	    			// get back the input stream
	    			qry = "select max(serial_no) as max from recmail";
	    			sta =con.createStatement();
	    			res = sta.executeQuery(qry);
	    			int index = 0;
	    			if(res.next())
	    				index = res.getInt("max");
	    			
	    			qry = "select attachment from recmail where serial_no ='"+index+"';";
	    			
	    			pr = con.prepareStatement(qry);
	    			res = pr.executeQuery();
	    			if(res.next())
	    			in_1 = res.getBinaryStream(1);
	    			
	    			qry = "insert into sendmail(senderEmail,receiveremail,subject,message,attachment,fileName,type) values(?,?,?,?,?,?,?);";
	    			pr = con.prepareStatement(qry);
	                pr.setString(1,user);
	                pr.setString(2, Rec);
	                pr.setString(3,Subject);
	                pr.setString(4, Message);
	                pr.setBinaryStream(5, in_1);
	                pr.setString(6,file_name);
	                pr.setString(7, file_type);
	    			pr.executeUpdate();
	    			return true;
		            }
			//--------------------------------------------------
			
			qry = "insert into recmail(senderEmail,receiveremail,subject,message) values('"+user+"','"+Rec+"','"+Subject+"','"+Message+"');";
			sta.execute(qry);
	
			qry = "insert into sendmail(senderEmail,receiveremail,subject,message) values('"+user+"','"+Rec+"','"+Subject+"','"+Message+"');";
			sta.execute(qry);
			
			qry = "Update totalsr Set Sender = Sender + 1 where emailid = '"+user+"';";
			sta.execute(qry);
		
			qry = "Update totalsr Set receiver = receiver + 1 where emailid = '"+Rec+"';";
			sta.execute(qry);
		
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	public static ResultSet email() {
		ResultSet a = null;
		return a;
	}
	public static void verify_password(PrintWriter out, BufferedReader in) {
		try {
			
			String pass = in.readLine();
			String qry = "Select password from users where password = '"+pass+"';";
			sta = con.createStatement();
			res = sta.executeQuery(qry);
			if(!res.next()) {
			out.println("invalid");
			return;
			}
			if(pass.equals(res.getString(1))) {
				out.println("confirm");
				
			}
			else out.println("No-confirm");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void update_password(PrintWriter out, BufferedReader in) {
		try {
			String user = in.readLine();
			String qry = "update users Set password = '"+in.readLine()+"' where EmailID = '"+ user+"';";
			sta = con.createStatement();
			sta.execute(qry);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void  verify_user(PrintWriter out,BufferedReader in) {
		try {
			String user = in.readLine();
			String pass = in.readLine();
			String qry = "Select EmailID ,password from users where EmailID = '"+user+"';";
			sta = con.createStatement();
			res = sta.executeQuery(qry);
			if(!res.next()) {
			out.println("0");
			return;
			}
			if(user.equals(res.getString(1)) && pass.equals(res.getString(2))) {
				out.println("1");
				
			}
			else out.println("0");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
}
}