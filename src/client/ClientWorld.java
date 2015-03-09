package client;

import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

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
	private PacketListner listener;
	private PacketSender sender;
	private Input currentInput = new Input();
	private Input lastInput = new Input();
	
	private static class WorldSingletonHolder {
		public static final ClientWorld instance = new ClientWorld();
	}

	public static ClientWorld getInstance() {
		return WorldSingletonHolder.instance;
	}

	private double m_lastTime = System.currentTimeMillis();
	private double m_actualFps = 0.0;

	private final GameWindow m_gameWindow;
	private EntityManager entManager;
	private ClientWorld() {
		entManager = new EntityManager();
		m_gameWindow = new GameWindow(entManager);
		addKeyListener(this);
	}

	public void process() {
		initPlayers();
		while (true) {
			if (newFrame()) {
				// Only add changes to the message queue
				if(lastInput.update(currentInput)) {
					sender.addMessage(currentInput);
				}
				currentInput = new Input();
				
				MsgData data;
				while ((data = listener.getNextMsg()) != null) {
					handleMsg(data);
				}
				
				// TODO: Get State /Send input
				m_gameWindow.repaint();
				
				try {
					Thread.sleep(1000/60);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void handleMsg(MsgData msg) {
		if(msg == null) return;
		switch(msg.getType()) {
		case MsgData.SUBFRAME:
			handleMsg((Subframe) msg);
			break;
		case MsgData.CONNECTION: // ignore
			break;
		case MsgData.PACKAGE:
			handleMsg((MsgBundle) msg);
			break;
		default:
			System.err.println("ClientWorld.handleMsg() error " + msg.getType());
			break;
		}
	}
	
	private void handleMsg(MsgBundle msg) {
		for(MsgData m : msg.getPastMessages()) {
			handleMsg(m);
		}
	}
	
	private void handleMsg(Subframe msg) {
		
		LinkedList<GameEntity> ge = entManager.getState();
		for(int i = 0; i < 5; i++) {
			ge.get(i).updateTransformation(msg.get(i));
		}
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
		entManager.addShip(
				new Vector2D(Const.START_TEAM1_SHIP1_X,
						Const.START_TEAM1_SHIP1_Y),
				new Vector2D(0.0, 0.0),
				new Vector2D(1.0, 0.0),
				Const.TEAM1_COLOR,
				null, 0);

		entManager.addShip(
				new Vector2D(Const.START_TEAM1_SHIP2_X,
						Const.START_TEAM1_SHIP2_Y),
				new Vector2D(0.0, 0.0),
				new Vector2D(1.0, 0.0),
				Const.TEAM1_COLOR,
				null, 1);

		// Team 2
		entManager.addShip(
				new Vector2D(Const.START_TEAM2_SHIP1_X,
						Const.START_TEAM2_SHIP1_Y),
				new Vector2D(0.0, 0.0),
				new Vector2D(-1.0, 0.0),
				Const.TEAM2_COLOR,
				null, 2);

		entManager.addShip(
				new Vector2D(Const.START_TEAM2_SHIP2_X,
						Const.START_TEAM2_SHIP2_Y),
				new Vector2D(0.0, 0.0),
				new Vector2D(-1.0, 0.0),
				Const.TEAM2_COLOR,
				null, 3);

		// Ball
		entManager.addBall(
				new Vector2D(Const.BALL_X, Const.BALL_Y),
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