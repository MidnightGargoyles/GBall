package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

import shared.MsgData;
import shared.Util;

public class ClientConnection extends Thread {
	private int port;
	private InetAddress address;
	private DatagramSocket socket;
	
	private ConcurrentLinkedQueue<MsgData> messageQueue = new ConcurrentLinkedQueue<MsgData>();
	
	public ClientConnection(DatagramSocket socket, InetAddress address, int port) {
		
	}
	
	public void addMessage(MsgData s) {
		messageQueue.add(s);
	}
	
	public void run() {
		while(true) {
			MsgData d = messageQueue.poll();
			if (d == null) continue;
			
			byte[] buf = Util.pack(d);
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
