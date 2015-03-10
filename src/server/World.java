package server;

import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import GBall.Const;
import GBall.EntityManager;
import GBall.KeyConfig;
import GBall.Vector2D;
import shared.EntityTransformation;
import shared.Input;
import shared.MsgData;
import shared.Subframe;

public class World {

	public static final String SERVERIP = "127.0.0.1"; // 'Within' the emulator!
	public static final int SERVERPORT = 4444;

	private static class WorldSingletonHolder {
		public static final World instance = new World();
	}

	public static World getInstance() {
		return WorldSingletonHolder.instance;
	}

	private Input[] keyStates = { new Input(), new Input(), new Input(),
			new Input() };
	private EntityManager entManager;
	private double m_lastTime = System.currentTimeMillis();
	private double m_actualFps = 0.0;

	private World() {
		entManager = new EntityManager();
	}

	public void updateInputs(Input input) {
		keyStates[0].update(input);
	}

	public void initialize() {
		initPlayers();
	}

	public void process() {
		//if (newFrame()) {
			entManager.updatePositions();
			entManager.checkBorderCollisions(Const.DISPLAY_WIDTH,
					Const.DISPLAY_HEIGHT);
			entManager.checkShipCollisions();
			/*try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		//}
	}

	private boolean newFrame() {
		double currentTime = System.currentTimeMillis();
		double delta = currentTime - m_lastTime;
		boolean rv = (delta > Const.FRAME_INCREMENT);
		if (rv) {
			m_lastTime += Const.FRAME_INCREMENT;
			if (delta > 10 * Const.FRAME_INCREMENT) {
				m_lastTime = currentTime;
			}
			m_actualFps = 1000 / delta;
		}
		return rv;
	}

	private void initPlayers() {
		// Team 1
		entManager.addShip(new Vector2D(Const.START_TEAM1_SHIP1_X,
				Const.START_TEAM1_SHIP1_Y), new Vector2D(0.0, 0.0),
				new Vector2D(1.0, 0.0), Const.TEAM1_COLOR, keyStates[0], 0);

		entManager.addShip(new Vector2D(Const.START_TEAM1_SHIP2_X,
				Const.START_TEAM1_SHIP2_Y), new Vector2D(0.0, 0.0),
				new Vector2D(1.0, 0.0), Const.TEAM1_COLOR, keyStates[1], 1);

		// Team 2
		entManager.addShip(new Vector2D(Const.START_TEAM2_SHIP1_X,
				Const.START_TEAM2_SHIP1_Y), new Vector2D(0.0, 0.0),
				new Vector2D(-1.0, 0.0), Const.TEAM2_COLOR, keyStates[2], 2);

		entManager.addShip(new Vector2D(Const.START_TEAM2_SHIP2_X,
				Const.START_TEAM2_SHIP2_Y), new Vector2D(0.0, 0.0),
				new Vector2D(-1.0, 0.0), Const.TEAM2_COLOR, keyStates[3], 3);

		// Ball
		entManager.addBall(new Vector2D(Const.BALL_X, Const.BALL_Y),
				new Vector2D(0.0, 0.0), 4);
	}

	public double getActualFps() {
		return m_actualFps;
	}

	public void addKeyListener(KeyListener k) {

	}

	/**
	 * Whole state
	 */
	public void packageKeyframe() {

	}

	/**
	 * smaller package of changes
	 */
	public Subframe packageSubframe() {
		return new Subframe(entManager);
	}

}