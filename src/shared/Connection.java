package shared;

import java.net.InetAddress;
/**
 * Represents a connection request to the server. This is
 * an extension of the class <b>ATMMsg</b> - meaning it follows
 * the "At most once" protocol.
 * @author Niklas
 * @see ATMMsg
 */
public class Connection extends ATMMsg {
	//private int entID  = -1;
	//private boolean success = false;
	
	/**
	 * Constructs a new Connection request, using the provided address
	 * and port as it's origin.
	 * @param address - the source address of the request
	 * @param port - the source port of the request
	 */
	public Connection(InetAddress address, int port) {
		super(MsgData.CONNECTION, address, port);
		this.sourceAddr = address;
		this.sourcePort = port;
	}
	
	/*public Connection(int entID, boolean success, int srcPort) {
		super(MsgData.CONNECTION, null, srcPort);
		this.entID = entID;
		this.success = success;
	}*/
	
	/*public InetAddress getAddress() {
		return address;
	}*/
	
}
