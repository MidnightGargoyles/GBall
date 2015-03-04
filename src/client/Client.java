package client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import server.Server;
import shared.Connection;
import shared.MsgData;
import shared.PacketListner;
import shared.PacketSender;

public class Client {
	private DatagramSocket socket;
	private InetAddress address;
	public static final int server_port = 25041;
	private PacketSender sender;
	private PacketListner listener;
	public Client() {
		try {
			System.out.println("foo");
			address = InetAddress.getByName("localhost");
			socket = new DatagramSocket();
			System.out.println(socket.getLocalPort());
			sender = new PacketSender(socket, address, server_port);
			listener = new PacketListner(socket);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Server s = new Server();
		
		boolean r = authenticate();
	}
	
	public static void main(String [] args) {
		//StartupDialog d = new StartupDialog("HAI", "foo");
		StartupDialog.Result result = StartupDialog.showDialog();
		switch(result.type) {
		case StartupDialog.Result.CONNECT:
			
			break;
		case StartupDialog.Result.HOST:
			
			break;
		case StartupDialog.Result.EXIT:
			System.exit(0);
			break;
		}
		Client c = new Client();
		c.run();
		System.out.println("wat");
	}
	
	public void run() {
		while(true) {
			
		}
	}
	
	public boolean authenticate() {
		System.out.println("AUTH");
		
		System.out.println(socket.getPort());
		Connection c = new Connection(socket.getInetAddress(), socket.getLocalPort());
		sender.addMessage(c);
		MsgData d;
		while((d = listener.getNextMsg()) == null) {
		}
		System.out.println("yes");
		return true;
	}
}
