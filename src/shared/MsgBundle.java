package shared;

import java.util.Date;

public class MsgBundle extends MsgData {
	/**
	 * 3 is latest, 0 is oldest
	 */
	private MsgData[] pastMessages = new MsgData[4];
	public MsgBundle(MsgData pastMessages0, MsgData pastMessages1, MsgData pastMessages2, MsgData pastMessages3) {
		this();
		this.pastMessages[0] = pastMessages0;
		this.pastMessages[1] = pastMessages1;
		this.pastMessages[2] = pastMessages2;
		this.pastMessages[3] = pastMessages3;
	}
	public MsgBundle() {
		super(MsgData.PACKAGE);
	}
	
	public void refreshStamp() {
		index = new Date();
	}
	public void addNext(MsgData msg) {
		
		for(int i = 0; i < pastMessages.length-1 ; i++) {
			pastMessages[i] = pastMessages[i+1];
		}
		pastMessages[3] = msg;
	}
	
	public MsgData[] getPastMessages() {
		return pastMessages;
	}
}
