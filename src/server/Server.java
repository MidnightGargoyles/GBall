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
import shared.MsgData;
import shared.PacketListner;
import shared.PacketSender;

public class Server extends Thread {
	private DatagramSocket socket;
	private PacketListner listener;
	private ArrayList<PacketSender> clients = new ArrayList<PacketSender>();

	public Server() {
		try {
			socket = new DatagramSocket(Client.server_port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		listener = new PacketListner(socket);
		start();
	}

	public void run() {
		while (true) {
			MsgData data;
			while ((data = listener.getNextMsg()) != null) {
				if (data.getType() == MsgData.CONNECTION) {
					Connection req = (Connection) data;
					System.out.println("YAY");
					Connection c = new Connection(0, true);
					PacketSender ps = new PacketSender(socket,
							req.getAddress(), req.getPort());
					clients.add(ps);
					ps.addMessage(c);
				}
			}
			// TODO proccess all incoming

			// TODO simulate world

			// TODO send messages
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

}
