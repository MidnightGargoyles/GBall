package shared;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class PacketListner extends Thread {
	private AtomicBoolean alive = new AtomicBoolean(true);
	private ConcurrentLinkedQueue<MsgData> messageQueue = new ConcurrentLinkedQueue<MsgData>();
	private Date latest = null;
	private DatagramSocket socket;
	
	/**
	 * Creates a PacketListner using the specified socket.
	 * @param socket - The socket to use.
	 */
	public PacketListner(DatagramSocket socket) {
		this.socket = socket;
		start();
	}
	
	/**
	 * Creates a PacketListner using the specified socket and
	 * using the specified name as the thread name.
	 * @param socket - The socket to use.
	 * @param threadName - The name for the thread.
	 */
	public PacketListner(DatagramSocket socket, String threadName) {
		this(socket);
		setName(threadName);
	}
	
	public void run() {
		// TODO
		while(alive.get()) {
			byte[] bytes = new byte[1024];
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
			try {
				socket.receive(packet);
				MsgData msg = Util.unpack(packet.getData());
				if(msg.getType() == MsgData.PACKAGE) {
					addIfLegit((Package)msg);
				} else {
					addIfLegit(msg);
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addIfLegit(MsgData msg) {
		MsgData last = null;
		for(MsgData m : messageQueue) {
			last = m;
		}
		if(last != null) {
			if(msg.greaterThan(last)){
				messageQueue.add(msg);
			}
		} else if(latest != null) {
			if(msg.getTimestamp().after(latest)){
				messageQueue.add(msg);
			}
		} else {
			messageQueue.add(msg);
		}
	}
	
	private void addIfLegit(Package msg) {
		MsgData[] pack =  msg.getPastMessages();
		
		MsgData last = null;
		for(MsgData m : messageQueue) {
			last = m;
		}
		if(last != null) {
			for(int i = 0; i < pack.length; i++) {
				if(pack[i].greaterThan(last)){
					messageQueue.add(pack[i]);
				}
			}
		} else if(latest != null) {
			for(int i = 0; i < pack.length; i++) {
				if(pack[i].getTimestamp().after(latest)){
					messageQueue.add(pack[i]);
				}
			}
		} else {
			for(int i = 0; i < pack.length; i++) {
				messageQueue.add(pack[i]);
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
