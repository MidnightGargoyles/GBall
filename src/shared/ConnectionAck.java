package shared;

import java.net.InetAddress;
import java.util.Date;

public class ConnectionAck extends MsgAck {
	public final int entID;
	public final boolean success;
	
	public ConnectionAck(int id, InetAddress origin, int entID, boolean success) {
		super(MsgData.CONNECTION, id, origin, entID, false);
		this.entID = entID;
		this.success = success;
	}
}
