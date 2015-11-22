import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
	private static int ID = 1;
	private int downloadPort;
	private int uploadPort;
	private int serverPort;
	private String serverIP;
	private int id;

	public Client(int downloadPort, int uploadPort, String serverIP,
			int serverPort) {
		id = ID++;
		this.downloadPort = downloadPort;
		this.uploadPort = uploadPort;
		this.serverIP = serverIP;
		this.serverPort = serverPort;
	}
	

	public void run() {
		setupConnection();
	}

	
	public int getClientId(){
		return id;
	}
	
	private void setupConnection() {
		try {
			Socket uploadSock = new Socket(serverIP, serverPort);
			PrintWriter writer = new PrintWriter(uploadSock.getOutputStream());
			writer.write(getClientId()+"\n");
			writer.flush();
			writer.close();
			uploadSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
