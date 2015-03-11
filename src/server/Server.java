package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import client.Client;
import shared.ATMMsg;
import shared.Connection;
import shared.ConnectionAck;
import shared.Input;
import shared.MsgAck;
import shared.MsgBundle;
import shared.MsgData;
import shared.PacketListner;
import shared.PacketSender;
import shared.RespondOrder;
import shared.Input.KeyState;
import shared.StoppableThread;

public class Server extends StoppableThread {
	public static final int TPS = 25;
	public static final float SENDS_SECOND = 25f;
	private DatagramSocket socket;
	private PacketListner listener;
	private ArrayList<PacketSender> clients = new ArrayList<PacketSender>();
	private HashMap<InetAddress, Date> lastTimeStamps = new HashMap<InetAddress, Date>();
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
	private int c = 0;
	public void run() {
		World.getInstance().initialize();
		while (alive.get()) {
			long start = System.nanoTime();
			MsgData data;
			while ((data = listener.getNextMsg()) != null) {
				handleMsg(data);
			}
			// TODO proccess all incoming

			World.getInstance().process();
			if(c++ >= TPS/SENDS_SECOND) {
				c = 0;
				for(int i = 0; i < clients.size(); i++) {
					clients.get(i).addMessage(World.getInstance().packageSubframe());
				}
			}
			
			long end = System.nanoTime();
			long elapsed = end - start;
			
			if(TPS - elapsed/1000 > 0) {
				try {
					
					Thread.sleep(TPS - elapsed/1000);
				} catch (InterruptedException e) {
					continue;
				}
			}
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
	
	/**
	 * Figures out the type of the message
	 * and performs the neccessary operations.
	 * @param msg
	 */
	private void handleMsg(MsgData msg) {
		if(msg == null) return;
		//System.out.println(msg + " <<<<");
		/*if(msg.getType() == MsgData.PACKAGE) { // special case to ignore timestamps
			handleMsg((MsgBundle)msg);
			return;
		}*/
		Date d = lastTimeStamps.get(msg.getSource());
		if(!msg.greaterThan(d)) return;
		
		switch (msg.getType()) {
		case MsgData.CONNECTION:
			handleMsg((Connection)msg);
			break;
		case MsgData.INPUT:
			handleMsg((Input)msg);
			break;
		case MsgData.PACKAGE:
			handleMsg((MsgBundle)msg);
			break;
		case MsgData.ACK:
			handleMsg((MsgAck) msg);
			break;
		case MsgData.RESPOND:
			handleMsg((RespondOrder) msg);
			break;
		default:
			System.err.println("Uknown message type: " + msg.getType() + " (Server.handleMsg())");
			return; // abort
		}
		// Do this last as we want to process bundles recursively
		if(msg.greaterThan(d)) {
			lastTimeStamps.put(msg.getSource(), msg.getTimestamp());
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
		//Connection c = new Connection(0, true, socket.getLocalPort());
		PacketSender ps = null;
		for(PacketSender p : clients) {
			if(p.matches(msg.getSource(), msg.getSourcePort())) {
				ps = p;
				break;
			}
		}
		if(ps == null) {
			ps = new PacketSender(socket, msg.getSourceAddress(),
					msg.getSourcePort(), "Server_Sender_" + msg.getSourceAddress());
			clients.add(ps);
		}
		ConnectionAck c = new ConnectionAck(msg.id, msg.getSource(), 1337, true);
		ps.addMessage(c);
		
	}
	
	private void handleMsg(Input msg) {
		//System.out.println("forward: " + msg.forward + " " + msg.getTimestamp().getTime());
		World.getInstance().updateInputs(msg);
	}
	
	private void handleMsg(RespondOrder msg) {
		/*for(PacketSender c : clients) {
			//System.out.println(c.getTargetAddress().toString() + " == " + msg.getSource().toString());
			if(c.getTargetAddress().equals(msg.getSource())) {
				//System.out.println("RESPONDED");
				c.addMessage(new MsgAck(msg.getTimestamp(), msg.getSource()));
			}
		}*/
	}
	
	private void handleMsg(MsgAck msg) {
		for(PacketSender c : clients) {
			if(c.getTargetAddress().equals(msg.getSourceAddress()) && c.getTargetPort() == msg.getSourcePort()) {
				c.notifyATMResponse(msg);
			}
		}
	}
}
