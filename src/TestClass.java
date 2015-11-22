class TestClass {

	public static void main(String args[]) throws Exception {
		Server s=new Server(5000, "C:\\install.exe");
		s.start();
		Client client1=new Client(6000, 6001, "127.0.0.1", 5000);
		Client client2=new Client(6000, 6001, "127.0.0.1", 5000);
		Client client3=new Client(6000, 6001, "127.0.0.1", 5000);
		Client client4=new Client(6000, 6001, "127.0.0.1", 5000);
		Client client5=new Client(6000, 6001, "127.0.0.1", 5000);
		client1.start();
		client2.start();
		client3.start();
		client4.start();
		client5.start();
	}
}
