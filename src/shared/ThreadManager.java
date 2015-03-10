package shared;

import java.util.ArrayList;
/**
 * Allows killing all threads from anywhere
 * @author Niklas
 *
 */
public class ThreadManager {
	private static final ThreadManager instance = new ThreadManager();
	private ArrayList<StoppableThread> threads = new ArrayList<StoppableThread>();
	public static ThreadManager inst() {
		return instance;
	}
	
	public void addToWatch(StoppableThread t) {
		threads.add(t);
	}
	
	/**
	 * Kills and forgets
	 */
	public void killAll() {
		StoppableThread t;
		while(!threads.isEmpty()) {
			t = threads.remove(0);
			t.halt();
		}
	}
}
