package shared;

import java.net.InetAddress;
import java.util.Date;
/**
 * Represents the basic form of an Acknowledgement to a corresponding
 * <b>ATMMsg</b>.
 * @author Niklas
 * @see ATMMsg
 */
public class MsgAck extends MsgData {
	private int id;
	private InetAddress origin;
	private int srcPort;
	
	
	/**
	 * Constructor for extensions
	 * @param type - the type of this message, e.g. <b>MsgData</b>.<b><i>ACK</i></b>
	 * @param id - the id of the <b>ATMMsg</b> this is responding to
	 * @param origin - the source address of the <b>ATMMsg</b> this is responding to
	 * @param srcPort - the source port of the <b>ATMMsg</b> this is responding to
	 * @param canPack
	 */
	protected MsgAck(int type, int id, InetAddress origin, int srcPort, boolean canPack) {
		super(type, Protocol.MAYBE, canPack);
		this.id = id;
		this.origin = origin;
		this.srcPort = srcPort;
		
	}
	
	/**
	 * Constructs a generic acknowledgment, using the type <b><i>ACK</i></b>.
	 * @param id - the id of the <b>ATMMsg</b> this is responding to
	 * @param origin - the source address of the <b>ATMMsg</b> this is responding to
	 * @param srcPort - the source port of the <b>ATMMsg</b> this is responding to
	 * @param canPack
	 */
	public MsgAck(int id, InetAddress origin, int srcPort, boolean canPack) {
		this(ACK, id, origin, srcPort, canPack);
		
	}
	
	/**
	 * Constructs a generic acknowledgment, using the type <b><i>ACK</i></b> and
	 * allowing packaging.
	 * @param id - the id of the <b>ATMMsg</b> this is responding to
	 * @param origin - the source address of the <b>ATMMsg</b> this is responding to
	 * @param srcPort - the source port of the <b>ATMMsg</b> this is responding to
	 */
	public MsgAck(int id, InetAddress origin, int srcPort) {
		this(ACK, id, origin, srcPort, true);
		
	}
	/**
	 * 
	 * @param msg an <b>ATMMsg</b> to compare to
	 * @return true if this message is a response to the given ATMMsg
	 */
	public boolean isResponseTo(ATMMsg msg) {
		return msg.id == id;
	}
	
	/**
	 * 
	 * @return the id this Ack is a response to
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * 
	 * @return the port of the <b>ATMMsg</b> this is responding to
	 */
	public int getSourcePort() {
		return srcPort;
	}
	
	/**
	 * 
	 * @return the address of the <b>ATMMsg</b> this is responding to
	 */
	public InetAddress getSourceAddress() {
		return origin;
	}
}
