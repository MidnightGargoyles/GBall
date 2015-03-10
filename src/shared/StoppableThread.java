package shared;

import java.util.concurrent.atomic.AtomicBoolean;

public class StoppableThread extends Thread {
	
	protected AtomicBoolean alive = new AtomicBoolean(true);
	
	protected StoppableThread() {
		super();
		ThreadManager.inst().addToWatch(this);
	}
	
	/**
	 * Sets an indicator for this thread to stop. The thread
	 * does not necessarily need to take this in regard.
	 */
	public void halt() {
		alive.set(false);
	}
}
