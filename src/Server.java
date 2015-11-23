import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
	private int port;
	private String fileSource;
	private final String splitFilesDestination = "c://splitFiles";
	private Socket[] clients = new Socket[5];
	double numFiles;
	//private long[][] filesAllotment;

	public Server(int port, String fileSource) {
		this.port = port;
		this.fileSource = fileSource;
	}

	public void run() {
		splitFiles(100 * 1024);
		//allotFilesToClients();
		setupConnection();
		try {
			sendFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				IOFunctions.copy(reader, writer, i * splitSize);
				writer.close();
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*private void allotFilesToClients() {
		long count = 1;
		filesAllotment = new long[(int) Math.ceil(numFiles
				/ (float) clients.length) + 1][clients.length + 1];
		for (int row = 1; row < filesAllotment.length && count <= numFiles; row++)
			for (int client = 1; client <= 5 && count <= numFiles; client++)
				filesAllotment[row][client] = count++;

	
		  for (int row = 0; row < filesAllotment.length; row++) { for (int
		  client = 0; client <= 5; client++)
		  System.out.print(filesAllotment[row][client] + "  ");
		  System.out.println(); }
		 
	}*/

	private void createEmptyFiles(String directory, double numFiles)
			throws IOException {
		File dest = new File(splitFilesDestination);
		if(!dest.mkdir())
		for (File file : dest.listFiles())
			file.delete();

		for (int i = 1; i <= numFiles; i++) {
			File tempFile = new File(splitFilesDestination + "//" + i);
			tempFile.createNewFile();
		}
	}
	
	private List<File> getFilesFor(int Clientid,int totalFiles){
		List<File> files=new ArrayList<File>();
		
		int temp=Clientid;
		while(temp<=totalFiles){
			files.add(new File(splitFilesDestination+"//"+temp));
			temp=+5;
		}
		return files;
	}

	private void sendFiles() throws IOException {
		FileSender sender;
		for(int i=0;i<clients.length;i++){
		sender=new FileSender(getFilesFor(i+1, (int)numFiles), clients[i]);
		sender.sendFiles();
		}
	}

	private void setupConnection() {
		try {
			ServerSocket serverSock = new ServerSocket(port);
			for (int i = 0; i < clients.length; i++) {
				Socket temp = serverSock.accept();
				String line = new BufferedReader(new InputStreamReader(
						temp.getInputStream())).readLine();
				int clientId = Integer.parseInt(line);
				clients[clientId - 1] = temp;
				System.out.println(clientId);
			}
			serverSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private class FileSender extends Thread {
		Socket clientSocket;
		List<File> files;

		public FileSender(List<File> files, Socket clientSocket) {
			this.clientSocket = clientSocket;
			this.files = files;
		}

		public void run() {
			try {
				sendFiles();
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void sendFiles() throws IOException {
			OutputStream os=clientSocket.getOutputStream();
			for(File file:files)
				sendFile(file,os);
			endFileSend(os);
		}
		
		private void sendFile(File file,OutputStream os) throws IOException{
			InputStream is=new FileInputStream(file);
			BufferedWriter write=new BufferedWriter(new OutputStreamWriter(os));
			write.write(file.getName()+"\n");
			IOFunctions.copy(is, os, 0);
			is.close();
		}
		
		private void endFileSend(OutputStream os) throws IOException{
			BufferedWriter write=new BufferedWriter(new OutputStreamWriter(os));
			write.write("Done"+"\n");
		}
	}
}
