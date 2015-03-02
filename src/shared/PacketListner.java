package shared;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class PacketListner extends Thread {
	private AtomicBoolean alive = new AtomicBoolean(true);
	private ConcurrentLinkedQueue<MsgData> messageQueue = new ConcurrentLinkedQueue<MsgData>();
	private DatagramSocket socket;
	
	public PacketListner(DatagramSocket socket) {
		this.socket = socket;
		start();
	}
	
	public void run() {
		// TODO
		while(alive.get()) {
			byte[] bytes = new byte[1024];
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
			try {
				socket.receive(packet);
				MsgData msg = Util.unpack(packet.getData());
				messageQueue.add(msg);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public MsgData getNextMsg() {
		return messageQueue.poll();
	}
	
	public void kill() {
		alive.set(false);
	}

}
