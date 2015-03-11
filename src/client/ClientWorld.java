package client;

import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import GBall.Const;
import GBall.EntityManager;
import GBall.GameEntity;
import GBall.GameWindow;
import GBall.KeyConfig;
import GBall.Vector2D;
import shared.Connection;
import shared.Input;
import shared.MsgBundle;
import shared.MsgData;
import shared.PacketListner;
import shared.PacketSender;
import shared.Subframe;

public class ClientWorld implements KeyListener {
	private static final int TIMEOUT_TIME_MS = 5000;
	private PacketListner listener;
	private PacketSender sender;
	private Input currentInput = new Input();
	private Input lastInput = new Input();
	private HashMap<InetAddress, Date> lastTimeStamps = new HashMap<InetAddress, Date>();

	private static class WorldSingletonHolder {
		public static final ClientWorld instance = new ClientWorld();
	}

	public static ClientWorld getInstance() {
		return WorldSingletonHolder.instance;
	}

	private double m_lastTime = System.currentTimeMillis();
	private double m_actualFps = 0.0;
	private long lastUpdate;
	private long lastSentMessage;
	private final GameWindow m_gameWindow;
	private EntityManager entManager;

	private ClientWorld() {
		entManager = new EntityManager();
		m_gameWindow = new GameWindow(entManager);
		addKeyListener(this);
	}

	public void process() {
		initPlayers();
		lastUpdate = System.currentTimeMillis();
		lastSentMessage = System.currentTimeMillis();
		while (true) {
			long start = System.nanoTime();
			// Only add changes to the message queue
			if (lastInput.update(currentInput)) {
				sender.addMessage(currentInput);
				lastSentMessage = System.currentTimeMillis();
			}
			currentInput = new Input();
			
			if(lastSentMessage + 200 < System.currentTimeMillis()) {
				System.out.println("CLIENT RESENDING");
				sender.resendMessages();
				lastSentMessage = System.currentTimeMillis();
			}
			
			MsgData data;
			while ((data = listener.getNextMsg()) != null) {
				handleMsg(data);
			}
			if( lastUpdate + TIMEOUT_TIME_MS < System.currentTimeMillis()) {
				sender.halt();
				JOptionPane.showMessageDialog(null, "You have been disconnected!");
				System.exit(1);
				// TODO add disconnection code here
			}

			entManager.updatePositions();
			entManager.checkBorderCollisions(Const.DISPLAY_WIDTH,
					Const.DISPLAY_HEIGHT);
			entManager.checkShipCollisions();
			m_gameWindow.repaint();

			long end = System.nanoTime();
			long elapsed = end - start;

			if (1000/60 - elapsed / 1000 > 0) {
				try {

					Thread.sleep(1000/60 - elapsed / 1000); // 40 tps
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void handleMsg(MsgData msg) {
		if (msg == null)
			return;
		System.out.println("CLIENT REFRESHED");
		lastUpdate = System.currentTimeMillis();
		Date d = lastTimeStamps.get(msg.getSource());
		if (!msg.greaterThan(d))
			return;
		switch (msg.getType()) {
		case MsgData.SUBFRAME:
			handleMsg((Subframe) msg);
			break;
		case MsgData.CONNECTION: // ignore
			break;
		case MsgData.PACKAGE:
			handleMsg((MsgBundle) msg);
			break;
		default:
			System.err
					.println("ClientWorld.handleMsg() error " + msg.getType());
			break;
		}
		if(msg.greaterThan(d)) {
			lastTimeStamps.put(msg.getSource(), msg.getTimestamp());
		}
	}

	private void handleMsg(MsgBundle msg) {
		for (MsgData m : msg.getPastMessages()) {
			handleMsg(m);
		}
	}

	private void handleMsg(Subframe msg) {

		LinkedList<GameEntity> ge = entManager.getState();
		for (int i = 0; i < 5; i++) {
			ge.get(i).updateTransformation(msg.get(i));
		}
		
		entManager.setScore(msg.getScoreAsVector());
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
				new Vector2D(1.0, 0.0), Const.TEAM1_COLOR, null, 0);

		entManager.addShip(new Vector2D(Const.START_TEAM1_SHIP2_X,
				Const.START_TEAM1_SHIP2_Y), new Vector2D(0.0, 0.0),
				new Vector2D(1.0, 0.0), Const.TEAM1_COLOR, null, 1);

		// Team 2
		entManager.addShip(new Vector2D(Const.START_TEAM2_SHIP1_X,
				Const.START_TEAM2_SHIP1_Y), new Vector2D(0.0, 0.0),
				new Vector2D(-1.0, 0.0), Const.TEAM2_COLOR, null, 2);

		entManager.addShip(new Vector2D(Const.START_TEAM2_SHIP2_X,
				Const.START_TEAM2_SHIP2_Y), new Vector2D(0.0, 0.0),
				new Vector2D(-1.0, 0.0), Const.TEAM2_COLOR, null, 3);

		// Ball
		entManager.addBall(new Vector2D(Const.BALL_X, Const.BALL_Y),
				new Vector2D(0.0, 0.0), 4);
	}

	public double getActualFps() {

		return m_actualFps;
	}

	public void addKeyListener(KeyListener k) {
		m_gameWindow.addKeyListener(k);
	}

	public void feed(PacketListner listener, PacketSender sender) {
		this.listener = listener;
		this.sender = sender;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		try {
			if (e.getKeyCode() == KeyEvent.VK_D) {
				currentInput.right = Input.KeyState.ON;
			} else if (e.getKeyCode() == KeyEvent.VK_A) {
				currentInput.left = Input.KeyState.ON;
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
				currentInput.forward = Input.KeyState.ON;
			}
		} catch (Exception x) {
			System.err.println(x);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		try {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			} else if (e.getKeyCode() == KeyEvent.VK_D) {
				currentInput.right = Input.KeyState.OFF;
			} else if (e.getKeyCode() == KeyEvent.VK_A) {
				currentInput.left = Input.KeyState.OFF;
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
				currentInput.forward = Input.KeyState.OFF;
			}
		} catch (Exception x) {
			System.err.println(x);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}