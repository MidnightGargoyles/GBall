package shared;

import java.net.InetAddress;
import java.util.Date;

public class MsgAck extends MsgData {
	private int id;
	private InetAddress origin;
	private int srcPort;
	
	
	
	public MsgAck(int type, int id, InetAddress origin, int srcPort, boolean canPack) {
		super(type, Protocol.MAYBE, false);
		this.id = id;
		this.origin = origin;
		this.srcPort = srcPort;
		
	}
	
	public MsgAck(int id, InetAddress origin, int srcPort, boolean canPack) {
		this(ACK, id, origin, srcPort, canPack);
		
	}
	
	public MsgAck(int id, InetAddress origin, int srcPort) {
		this(ACK, id, origin, srcPort, true);
		
	}
	
	public boolean isResponseTo(ATMMsg msg) {
		return msg.id == id;
	}
	
	public int getID() {
		return id;
	}
	
	public int getSourcePort() {
		return srcPort;
	}
	
	public InetAddress getSourceAddress() {
		return origin;
	}
}
