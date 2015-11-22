import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	private int port;
	private String fileSource;
	private final String splitFilesDestination = "c://splitFiles";
	private Socket[] clients = new Socket[5];
	double numFiles;
	private long[][] filesAllotment;

	public Server(int port, String fileSource) {
		this.port = port;
		this.fileSource = fileSource;
	}

	public void run() {
		splitFiles(100 * 1024);
		allotFilesToClients();
		setupConnection();
		// sendFiles();
	}

	private void splitFiles(int splitSize) {
		try {

			File src = new File(fileSource);
			FileInputStream reader = new FileInputStream(src);

			numFiles = Math.ceil(src.length() / (float) splitSize);

			createEmptyFiles(splitFilesDestination, numFiles);

			for (int i = 0; i < numFiles; i++) {
				FileOutputStream writer = new FileOutputStream(new File(
						splitFilesDestination + "//" + (i + 1)));
				copy(reader, writer, i * splitSize);
				writer.close();
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void allotFilesToClients() {
		long count = 1;
		filesAllotment = new long[(int) Math.ceil(numFiles
				/ (float) clients.length) + 1][clients.length + 1];
		for (int row = 1; row < filesAllotment.length && count <= numFiles; row++)
			for (int client = 1; client <= 5 && count <= numFiles; client++)
				filesAllotment[row][client] = count++;

		/*for (int row = 0; row < filesAllotment.length; row++) {
			for (int client = 0; client <= 5; client++)
				System.out.print(filesAllotment[row][client] + "  ");
			System.out.println();
		}*/
	}

	private void createEmptyFiles(String directory, double numFiles)
			throws IOException {
		File dest = new File(splitFilesDestination);
		dest.mkdir();
		for (File file : dest.listFiles())
			file.delete();

		for (int i = 1; i <= numFiles; i++) {
			File tempFile = new File(splitFilesDestination + "//" + i);
			tempFile.createNewFile();
		}
	}

	private void copy(InputStream in, OutputStream out, long skip)
			throws IOException {
		byte[] buf = new byte[100 * 1024];
		int len = 0;
		in.skip(skip);
		len = in.read(buf);
		out.write(buf, 0, len);
		in.skip(-1 * (skip + len));
	}

	private void sendFiles() {

	}

	private void setupConnection() {
		try {
			ServerSocket serverSock = new ServerSocket(port);
			for (int i = 0; i < clients.length; i++) {
				Socket temp = serverSock.accept();
				String line = new BufferedReader(new InputStreamReader(
						temp.getInputStream())).readLine();
				int clientId=Integer.parseInt(line);
				clients[clientId-1]=temp;
				System.out.println(clientId);
			}
			serverSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void send(String filePath, Socket clientSocket) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath)));
			// reader.r
			// PrintWriter writer = new
			// PrintWriter(clientSocket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
