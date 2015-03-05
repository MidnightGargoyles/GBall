package shared;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class PacketSender extends StoppableThread {
	
	private ConcurrentLinkedQueue<MsgData> messageQueue = new ConcurrentLinkedQueue<MsgData>();
	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	private MsgBundle bundle;
	
	public PacketSender(DatagramSocket socket, InetAddress address, int port) {
		this.socket = socket;
		this.address = address;
		this.port = port;
		this.bundle = new MsgBundle();
		start();
	}
	
	public PacketSender(DatagramSocket socket, InetAddress address, int port, String threadName) {
		this(socket, address, port);
		setName(threadName);
	}
	
	public void run() {
		while(alive.get()) {
			
			MsgData msg = messageQueue.poll();
			
			if (msg == null) continue;
			msg.setSource(socket.getLocalAddress());
			bundle.addNext(msg);
			byte[] buf = Util.pack(bundle);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
			
			try {
				socket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addMessage(MsgData msg) {
		messageQueue.add(msg);
	}
	
	
	public void kill() {
		alive.set(false);
	}
}
