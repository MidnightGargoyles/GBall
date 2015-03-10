package shared;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;
import java.util.Random;
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
		this.bundle.setSource(address);
		start();
	}
	
	public PacketSender(DatagramSocket socket, InetAddress address, int port, String threadName) {
		this(socket, address, port);
		setName(threadName);
	}
	
	public void run() {
		while(alive.get()) {
			
			MsgData msg = messageQueue.peek();
			
			if (msg == null) continue;
			msg.setSource(socket.getLocalAddress());
			//System.out.println(getName() + " sent: " + msg);
			//System.out.println(msg.protocol);
			switch(msg.protocol) {
			case AT_MOST_ONCE:
				msg.refreshStamp();
				break;
			case MAYBE:
				messageQueue.poll();
				break;
			default:
				System.err.println("Faulty message protocol: " + msg.protocol + " (PacketSender.run())");
				return;
			}
			
			if(msg.canPack) {
				bundle.addNext(msg);
				bundle.refreshStamp();
				msg = bundle;
			}
			Random r = new Random();
			if(r.nextInt(100) > 0) { // imaginary packet loss rate
				byte[] buf = Util.pack(msg);
				DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
				
				try {
					socket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		System.out.println(getName() + " added: " + msg);
		messageQueue.add(msg);
	}
	
	/**
	 * Notifies the PacketSender that an acknowledgement of transmission
	 * has been received for an at least once message.
	 * @param ack
	 */
	public void notifyATMResponse(MsgAck ack) {
		MsgData msg = messageQueue.peek();
		if(msg == null) return;
		if(msg.protocol == MsgData.Protocol.AT_MOST_ONCE && ack.isResponseTo((ATMMsg)msg)) {
			messageQueue.poll();
		}
	}
	
	public void kill() {
		alive.set(false);
	}
	
	public InetAddress getTargetAddress() {
		return address;
	}
	public int getTargetPort() {
		return port;
	}
	/**
	 * has same target
	 */
	public boolean matches(InetAddress addr, int port) {
		return address.equals(addr) && this.port == port;
	}
}
