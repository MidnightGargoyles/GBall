package shared;

import java.net.InetAddress;
import java.util.Date;

public class RespondOrder extends MsgData {
	public final ATMMsg msg;
	public RespondOrder(ATMMsg msg) {
		super(RESPOND);
		this.msg = msg;
	}
	
	@Override
	public InetAddress getSource() {
		return msg.getSource();
	}
	
	@Override
	public Date getTimestamp() {
		return msg.getTimestamp();
	}

}
