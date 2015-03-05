package shared;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

public class MsgData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7883236279128653501L;
	
	public static final int KEYFRAME = 0;
	public static final int SUBFRAME = 1;
	public static final int CONNECTION = 3;
	public static final int PACKAGE = 4;
	public static final int INPUT = 5;
	
	protected int type;
	private Date index;
	private InetAddress source = null;
	
	public MsgData(int type) {
		this.type = type;
		this.index = new Date();
	}
	
	public int getType() {
		return type;
	}
	
	public boolean greaterThan(MsgData data) {
		return index.after(data.index);
	}
	
	public boolean lessThan(MsgData data) {
		return index.before(data.index);
	}
	public Date getTimestamp() {
		return index;
	}
	
	/**
	 * Is implicitly set during sending
	 * @param addr
	 */
	public void setSource(InetAddress addr) {
		source = addr;
	}
	
	/**
	 * Get the address of the last person to send this message.
	 * @return InetAddress - the address of the latest sender.
	 */
	public InetAddress getSource() {
		return source;
	}
}
