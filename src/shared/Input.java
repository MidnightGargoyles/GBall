package shared;

public class Input extends MsgData {
	public boolean forward;
	public boolean left;
	public boolean right;
	
	public Input(boolean forward, boolean left, boolean right) {
		super(INPUT);
		this.forward = forward;
		this.left = left;
		this.right = right;
		
	}
	
}
