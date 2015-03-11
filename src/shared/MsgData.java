package shared;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

public class MsgData implements Serializable {
	public enum Protocol {
		MAYBE, AT_MOST_ONCE;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 7883236279128653501L;
	
	public static final int KEYFRAME = 0;
	public static final int SUBFRAME = 1;
	public static final int CONNECTION = 3;
	public static final int PACKAGE = 4;
	public static final int INPUT = 5;
	/**
	 * A generic ack that contains no additional information
	 */
	public static final int ACK = 6;
	public static final int RESPOND = 7;
	public static final int END_ACK = 8;
	
	public final Protocol protocol;
	public final boolean canPack;
	protected int type;
	protected Date index;
	private InetAddress source = null;
	private int port = -1;
	
	/**
	 * Constructs a message, defaulting to using the <b>MAYBE</b> protocol.
	 * @param type
	 */
	protected MsgData(int type) {
		this(type, Protocol.MAYBE, true);
	}
	
	/**
	 * Constructs a message, following the specified protocol.
	 * @param type
	 * @param protocol
	 */
	protected MsgData(int type, Protocol protocol, boolean canPack) {
		this.type = type;
		this.index = new Date();
		this.protocol = protocol;
		this.canPack = canPack;
	}
	
	public int getType() {
		return type;
	}
	
	public boolean greaterThan(MsgData data) {
		return greaterThan(data.index);
	}
	
	public boolean greaterThan(Date date) {
		if(date == null) return true;
		return index.after(date);
	}
	
	public boolean lessThan(MsgData data) {
		return lessThan(data.index);
	}
	
	public boolean lessThan(Date date) {
		if(date == null) return false;
		return index.before(date);
	}
	public Date getTimestamp() {
		return index;
	}
	public void refreshStamp() {
		index = new Date();
	}
	
	/**
	 * Is implicitly set during sending
	 * @param addr
	 */
	public void setSource(InetAddress addr) {
		source = addr;
	}
	
	public void setSourcePort(int port) {
		this.port = port;
	}
	
	/**
	 * Get the address of the last person to send this message.
	 * @return InetAddress - the address of the latest sender.
	 */
	public InetAddress getSource() {
		return source;
	}
	
	public int getSourcePort() {
		return port;
	}
}
