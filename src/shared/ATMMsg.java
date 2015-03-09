package shared;

import java.net.InetAddress;

/**
 * A message that follows the "At most once" protocol
 * @author Niklas
 *
 */
public class ATMMsg extends MsgData {
	
	private static int counter = 0;
	/**
	 * Source of message
	 */
	protected InetAddress sourceAddr = null;
	
	/**
	 * source port of message
	 */
	protected int sourcePort = -1;
	public final int id;
	public ATMMsg(int type, InetAddress address, int port) {
		super(type, Protocol.AT_MOST_ONCE, false);
		this.sourceAddr = address;
		this.sourcePort = port;
		id = counter++;
		
	}
	public int getSourcePort() {
		return sourcePort;
	}
	
	public InetAddress getSourceAddress() {
		return sourceAddr;
	}
}
