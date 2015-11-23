import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	private final String filesDestination = "c://client";
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

	public int getClientId() {
		return id;
	}

	private void setupConnection() {
		try {
			setupServerConnection();
			setupUploadConnection();
			setupDownloadConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void setupServerConnection() throws UnknownHostException,
			IOException {
		Socket sock = new Socket(serverIP, serverPort);
		InputStream is = sock.getInputStream();
		BufferedReader reader=new BufferedReader(new InputStreamReader(is));
		String fileName;
		while((fileName=reader.readLine())!="Done"){
		String fileName = 
				.readLine();
		}
		/*
		 * PrintWriter writer = new PrintWriter(sock.getOutputStream());
		 * writer.write(getClientId() + "\n"); writer.flush(); writer.close();
		 */
		sock.close();
	}

	private void emptyOrCreateDirectory(String directory) {
		File dest = new File(directory);
		if (!dest.mkdir())
			for (File file : dest.listFiles())
				file.delete();
	}

	private void copyFilesTo(String directory,InputStream is) {
	}

	private void setupUploadConnection() {
	}

	private void setupDownloadConnection() {
	}
}
