package shared;

public class MsgBundle extends MsgData {
	private MsgData[] pastMessages = new MsgData[4];
	public MsgBundle(MsgData pastMessages0, MsgData pastMessages1, MsgData pastMessages2, MsgData pastMessages3) {
		super(MsgData.PACKAGE);
		this.pastMessages[0] = pastMessages0;
		this.pastMessages[1] = pastMessages1;
		this.pastMessages[2] = pastMessages2;
		this.pastMessages[3] = pastMessages3;
	}
	
	
	public MsgData[] getPastMessages() {
		return pastMessages;
	}
}
