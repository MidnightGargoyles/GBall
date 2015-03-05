package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import client.Client;
import shared.Connection;
import shared.Input;
import shared.MsgBundle;
import shared.MsgData;
import shared.PacketListner;
import shared.PacketSender;
import shared.Input.KeyState;
import shared.StoppableThread;

public class Server extends StoppableThread {
	private DatagramSocket socket;
	private PacketListner listener;
	private ArrayList<PacketSender> clients = new ArrayList<PacketSender>();
	private Input[] playerInputs = { 
			new Input(KeyState.OFF), new Input(KeyState.OFF), 
			new Input(KeyState.OFF), new Input(KeyState.OFF) };

	public Server() {
		try {
			socket = new DatagramSocket(Client.server_port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		listener = new PacketListner(socket, "Server_Listener");
		start();
	}

	public void run() {
		while (alive.get()) {
			MsgData data;
			while ((data = listener.getNextMsg()) != null) {
				handleMsg(data);
			}
			// TODO proccess all incoming

			// TODO simulate world

			// TODO send messages
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		 * try { ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 * DatagramSocket m_socket = new DatagramSocket(); InetAddress
		 * m_serverAddress = InetAddress.getByName("localhost");
		 * ObjectOutputStream oos = new ObjectOutputStream(baos);
		 * oos.writeObject(new MsgData()); oos.flush();
		 * 
		 * byte[] buf = new byte[1024];
		 * 
		 * buf = baos.toByteArray();
		 * 
		 * DatagramPacket pack = new DatagramPacket(buf, buf.length,
		 * m_serverAddress, SERVERPORT); m_socket.send(pack);
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}
	
	/**
	 * Figures out the type of the message
	 * and performs the neccessary operations.
	 * @param msg
	 */
	private void handleMsg(MsgData msg) {
		switch (msg.getType()) {
		case MsgData.CONNECTION:
			handleMsg((Connection)msg);
			break;
		case MsgData.PACKAGE:
			handleMsg((MsgBundle)msg);
			break;
		case MsgData.INPUT:
			handleMsg((Input)msg);
			break;
		}
	}
	
	/**
	 * handles all the messages contained in the bundle.
	 * @param msg
	 */
	private void handleMsg(MsgBundle msg) {
		for(MsgData m : msg.getPastMessages()) {
			handleMsg(m);
		}
	}

	/**
	 * Handles a connecion request message, establishing a packetsender
	 * for the connection client and informing them of the success.
	 * @param msg
	 */
	private void handleMsg(Connection msg) {
		System.out.println("YAY");
		Connection c = new Connection(0, true);
		PacketSender ps = new PacketSender(socket, msg.getAddress(),
				msg.getPort(), "Server_Sender_" + msg.getAddress());
		clients.add(ps);
		ps.addMessage(c);
	}
	
	private void handleMsg(Input msg) {
		System.out.println(msg.forward);
	}

}
