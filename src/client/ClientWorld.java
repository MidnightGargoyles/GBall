package client;

import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import GBall.Const;
import GBall.EntityManager;
import GBall.GameWindow;
import GBall.KeyConfig;
import GBall.Vector2D;
import shared.MsgData;
import shared.PacketListner;
import shared.PacketSender;

public class ClientWorld {
	private PacketListner listener;
	private PacketSender sender;
	
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
	}

	public void process() {
		initPlayers();

		// Marshal the state
		/*try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DatagramSocket m_socket = new DatagramSocket();
			InetAddress m_serverAddress = InetAddress.getByName("localhost");
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(new MsgData());
			oos.flush();

			byte[] buf = new byte[1024];

			buf = baos.toByteArray();

			DatagramPacket pack = new DatagramPacket(buf, buf.length,
					m_serverAddress, SERVERPORT);
			m_socket.send(pack);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		while (true) {
			if (newFrame()) {
				// TODO: Get State /Send input
				m_gameWindow.repaint();
			}
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
				new KeyConfig(KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_S,
						KeyEvent.VK_W));

		entManager.addShip(
				new Vector2D(Const.START_TEAM1_SHIP2_X,
						Const.START_TEAM1_SHIP2_Y),
				new Vector2D(0.0, 0.0),
				new Vector2D(1.0, 0.0),
				Const.TEAM1_COLOR,
				new KeyConfig(KeyEvent.VK_F, KeyEvent.VK_H, KeyEvent.VK_G,
						KeyEvent.VK_T));

		// Team 2
		entManager.addShip(
				new Vector2D(Const.START_TEAM2_SHIP1_X,
						Const.START_TEAM2_SHIP1_Y),
				new Vector2D(0.0, 0.0),
				new Vector2D(-1.0, 0.0),
				Const.TEAM2_COLOR,
				new KeyConfig(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
						KeyEvent.VK_DOWN, KeyEvent.VK_UP));

		entManager.addShip(
				new Vector2D(Const.START_TEAM2_SHIP2_X,
						Const.START_TEAM2_SHIP2_Y),
				new Vector2D(0.0, 0.0),
				new Vector2D(-1.0, 0.0),
				Const.TEAM2_COLOR,
				new KeyConfig(KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_K,
						KeyEvent.VK_I));

		// Ball
		entManager.addBall(
				new Vector2D(Const.BALL_X, Const.BALL_Y),
				new Vector2D(0.0, 0.0));
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

}