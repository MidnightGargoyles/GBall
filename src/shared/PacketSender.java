package shared;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class PacketSender extends StoppableThread {
	
	private ConcurrentLinkedQueue<MsgData> messageQueue = new ConcurrentLinkedQueue<MsgData>();
	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	private Semaphore empty = new Semaphore(0);
	/**
	 * 0-99
	 */
	private static final int LOSS_RATE = 0;

	private MsgBundle bundle;
	
	public PacketSender(DatagramSocket socket, InetAddress address, int port) {
		this.socket = socket;
		this.address = address;
		this.port = port;
		this.bundle = new MsgBundle();
		this.bundle.setSource(address);
		this.bundle.setSourcePort(port);
		start();
	}
	
	public PacketSender(DatagramSocket socket, InetAddress address, int port, String threadName) {
		this(socket, address, port);
		setName(threadName);
	}
	
	public void run() {
		while(alive.get()) {
			try {
				empty.acquire();
			} catch (InterruptedException e1) {
				continue;
			}
			MsgData msg = messageQueue.peek();
			
			if (msg == null) { // just resend old
				bundle.refreshStamp();
				msg = bundle;
			} else {
				msg.setSource(socket.getLocalAddress());
				msg.setSourcePort(socket.getLocalPort());
				
				//System.out.println(getName() + " sent: " + msg);
				//System.out.println(msg.protocol);
				switch(msg.protocol) {
				case AT_MOST_ONCE:
					msg.refreshStamp();
					empty.release();
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
			}
			
			Random r = new Random();
			if(r.nextInt(100)+1 > LOSS_RATE) { // imaginary packet loss rate
				byte[] buf = Util.pack(msg);
				DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
				
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Adds a message to the queue, releasing the thread in
	 * the process.
	 * @param msg
	 */
	public void addMessage(MsgData msg) {
		//System.out.println(getName() + " added: " + msg);
		messageQueue.add(msg);
		empty.release();
		
	}
	
	public void resendMessages() {
		empty.release();
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
