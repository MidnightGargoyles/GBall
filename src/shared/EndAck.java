package shared;

import java.net.InetAddress;
import java.util.Date;

/**
 * The last ack to send
 * @author Niklas
 *
 */
public class EndAck extends MsgData {
	private Date id;
	private int srcPort;
	
	public EndAck(Date id, int srcPort) {
		super(END_ACK);
	}
	
	public Date getID() {
		return null;
	}
}
