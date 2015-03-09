package shared;

import java.net.InetAddress;

public class Connection extends ATMMsg {
	//private int entID  = -1;
	//private boolean success = false;
	
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
