package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import GBall.Const;
import server.Server;
import shared.Connection;
import shared.ConnectionAck;
import shared.Input;
import shared.MsgData;
import shared.PacketListner;
import shared.PacketSender;
import shared.ThreadManager;

public class Client {
	private DatagramSocket socket;
	private InetAddress address;
	public static final int server_port = 25041;
	private PacketSender sender;
	private PacketListner listener;
	
	/**
	 * Creates a client and a server
	 */
	public Client() {
		this("localhost");
		Server s = new Server();
	}

	/**
	 * Creates a client that connects to a remote host
	 * 
	 * @param hostName
	 */
	public Client(String hostName) {
		try {
			address = InetAddress.getByName(hostName);
			socket = new DatagramSocket();
			sender = new PacketSender(socket, address, server_port, "Client_Sender");
			listener = new PacketListner(socket, "Client_Listener");
			listener.setSender(sender);
			socket.connect(address, server_port);
		} catch (UnknownHostException e) {
			System.out.println("");
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public static void main(String [] args) {
		Client c;
		do {
			ThreadManager.inst().killAll();
			StartupDialog.Result result = StartupDialog.showDialog();
			switch(result.type) {
			case StartupDialog.Result.CONNECT:
				c = new Client(result.value);
				break;
			case StartupDialog.Result.HOST:
				c = new Client();
				break;
			case StartupDialog.Result.EXIT:
				return;
			default:
				System.out.println("Unknown result type: " + result.type);
				return;
			}
			
		} while(!c.authenticate());
		c.run();
	}

	public void run() {
		ClientWorld.getInstance().feed(listener, sender);
		ClientWorld.getInstance().process();
	}

	public boolean authenticate() {
		Connection c = new Connection(socket.getInetAddress(),
				socket.getLocalPort());
		sender.addMessage(c);
		MsgData d;
		long start =  System.currentTimeMillis();
		while ((d = listener.getNextMsg()) == null || d.getType() != MsgData.CONNECTION && listener.isAlive()) {
			if(start + 15000 < System.currentTimeMillis()) return false;
		}
		if(!listener.isAlive()) return false;
		if(d.getType() == MsgData.CONNECTION) {
			ConnectionAck ac = (ConnectionAck) d;
			sender.notifyATMResponse(ac);
			return true;
		}
		System.out.println("yes");
		return false;
	}
	
	
	
	
}
