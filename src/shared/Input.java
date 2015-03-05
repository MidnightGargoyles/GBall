package shared;

public class Input extends MsgData {
	public enum KeyState {
		ON, OFF, NO_CHANGE;
	}
	public KeyState forward;
	public KeyState left;
	public KeyState right;
	
	/**
	 * Creates an Input object with all keys set to the specified
	 * states.
	 * @param forward - state to assign to the forward key.
	 * @param left - state to assign to the left key.
	 * @param right - state to assign to the right key.
	 */
	public Input(KeyState forward, KeyState left, KeyState right) {
		super(INPUT);
		this.forward = forward;
		this.left = left;
		this.right = right;
		
	}
	
	/**
	 * Creates an Input object with all keys set to the default
	 * state <b>NO_CHANGE</b>.
	 */
	public Input() {
		super(INPUT);
		this.forward = KeyState.NO_CHANGE;
		this.left = KeyState.NO_CHANGE;
		this.right = KeyState.NO_CHANGE;
	}
	
	/**
	 * Creates an Input object with all keys set to the
	 * specific state.
	 * @param state - the state to set all keys to.
	 */
	public Input(KeyState state) {
		super(INPUT);
		this.forward = state;
		this.left = state;
		this.right = state;
	}
	
	/**
	 * Updates this object, replacing the states with the
	 * new states if the new states aren't the type <b>NO_CHANGE</b>
	 * @param newState
	 */
	public void update(Input newState) {
		forward = (newState.forward == KeyState.NO_CHANGE) ? forward : newState.forward;
		left = (newState.left == KeyState.NO_CHANGE) ? left : newState.left;
		right = (newState.right == KeyState.NO_CHANGE) ? right : newState.right;
	}

	public boolean similarAs(Input input) {
		return (forward == input.forward || input.forward == KeyState.NO_CHANGE) &&
				(left == input.left || input.left == KeyState.NO_CHANGE) && 
				(right == input.right || input.right == KeyState.NO_CHANGE);
	}
	
}
