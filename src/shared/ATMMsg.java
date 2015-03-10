package shared;

import java.net.InetAddress;

/**
 * A message that follows the "At most once" protocol, making it "stick" in the
 * sender queue until a corresponding <b>MsgAck</b> has been received.
 * @author Niklas
 * @see MsgAck
 */
public class ATMMsg extends MsgData {
	/**
	 * determines the id generated messages obtain
	 */
	private static int counter = 0;
	/**
	 * Source of message
	 */
	protected InetAddress sourceAddr = null;
	
	/**
	 * source port of message
	 */
	protected int sourcePort = -1;
	/**
	 * The id of this message
	 */
	public final int id;
	/**
	 * 
	 * @param type - the type of this message
	 * @param address - the source address of this message
	 * @param port - the source port of this message
	 */
	protected ATMMsg(int type, InetAddress address, int port) {
		super(type, Protocol.AT_MOST_ONCE, false);
		this.sourceAddr = address;
		this.sourcePort = port;
		id = counter++;
		
	}
	
	
		
	
	
	/**
	 * 
	 * @return the source port of this message
	 */
	public int getSourcePort() {
		return sourcePort;
	}
	
	/**
	 * 
	 * @return the source address of this message
	 */
	public InetAddress getSourceAddress() {
		return sourceAddr;
	}
}
