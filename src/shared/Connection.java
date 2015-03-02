package shared;

import java.net.InetAddress;

public class Connection extends MsgData{
	private int entID  = -1;
	private boolean success = false;
	private InetAddress address = null;
	private int port = -1;
	
	public Connection(InetAddress address, int port) {
		super(MsgData.CONNECTION);
		this.address = address;
		this.port = port;
	}
	
	public Connection(int entID, boolean success) {
		super(MsgData.CONNECTION);
		this.entID = entID;
		this.success = success;
	}
	
	public int getPort() {
		return port;
	}
	
	public InetAddress getAddress() {
		return address;
	}
}
