import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
public class Threaded_Class implements Runnable {
	protected Socket client;
	public Threaded_Class(Socket s) {
		this.client = s;
	}
	public void run() {
		
		try {
			
			BufferedReader in;
			InputStreamReader is = new InputStreamReader(client.getInputStream());
			in = new BufferedReader(is);
			PrintWriter out = new PrintWriter(client.getOutputStream(),true);
			// Now Parser
			int task = Integer.parseInt(in.readLine());
			while(task != 0) {
				System.out.println("Task assign "+task);
				parser(out,in);
				task--;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void parser(PrintWriter out,BufferedReader in) {
		
		String KeyWord;
		try {
			KeyWord = in.readLine();
			System.out.println("RequesT For:"+KeyWord);
			if(KeyWord.equals("Login")) {
				helper.verify_user(out,in);
			}
			else if(KeyWord.equals("verify")) {
				helper.verify_password(out,in);
			}
			else if(KeyWord.equals("Update_P")) {
				helper.update_password(out, in);
			}
			else if(KeyWord.equals("MailDelivery")) {
				helper.MailDelivery(out,in);
			}
			else if(KeyWord.equals("ExtractEmail")) {
				helper.InboxReader(in.readLine(),in, out);
			}
			else if(KeyWord.equals("CreateAccount")) {
			System.out.println("Creating Account");
			if(helper.CreateAccount(in.readLine(), in.readLine()))
				out.println("Negative"); // user Already Exist
			else out.println("Non-Negative");
			}
			else if(KeyWord.equals("FileName")) {
			
				helper.file_name(out, in);
			}
			else if(KeyWord.equals("BLOB_DATA")) {
				helper.Extracting_Blob_data(out, in);
			}
		   else if(KeyWord.equals("Delete_Message")) {
			   helper.Delete_Message(in, out);
		   }
			
		}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}