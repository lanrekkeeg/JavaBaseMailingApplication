import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	ServerSocket server = null;
	public void run() {
		
		try {
			server = new ServerSocket(49159);
		}
		catch(IOException e) {
			System.out.println("From Server Side:Change the port Number");
		}
			while(true) {
				try {
					Socket client = server.accept();
					Threaded_Class T = new Threaded_Class(client);
					new Thread(T).start();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
			}
	}
}
