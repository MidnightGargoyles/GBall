package GBall;

import java.awt.Color;
import java.awt.event.*;

import server.World;
import shared.EntityTransformation;
import shared.Input;

public class Ship extends GameEntity {

	private Color m_color;
	private Input m_keyConfig;
	private int rotation = 0; // Set to 1 when rotating clockwise, -1 when
								// rotating counterclockwise
	private boolean braking = false;


	
	public Ship(final Vector2D position, final Vector2D speed,
			final Vector2D direction, final Color col, Input kc, int id) {
		super(position, speed, direction, Const.SHIP_MAX_ACCELERATION,
				Const.SHIP_MAX_SPEED, Const.SHIP_FRICTION, id);
		m_color = col;
		m_keyConfig = kc;
		//World.getInstance().addKeyListener(this);
	}

	/*@Override
	public void keyPressed(KeyEvent e) {
		try {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			} else if (e.getKeyCode() == m_keyConfig.rightKey()) {
				rotation = 1;
			} else if (e.getKeyCode() == m_keyConfig.leftKey()) {
				rotation = -1;
			} else if (e.getKeyCode() == m_keyConfig.accelerateKey()) {
				setAcceleration(Const.SHIP_MAX_ACCELERATION);
			} else if (e.getKeyCode() == m_keyConfig.brakeKey()) {
				braking = true;
			}
		} catch (Exception x) {
			System.err.println(x);
		}
	}

	public void keyReleased(KeyEvent e) {
		try {
			if (e.getKeyCode() == m_keyConfig.rightKey() && rotation == 1) {
				rotation = 0;
			} else if (e.getKeyCode() == m_keyConfig.leftKey()
					&& rotation == -1) {
				rotation = 0;
			} else if (e.getKeyCode() == m_keyConfig.accelerateKey()) {
				setAcceleration(0);
			} else if (e.getKeyCode() == m_keyConfig.brakeKey()) {
				braking = false;
			}
		} catch (Exception x) {
			System.out.println(x);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}*/

	@Override
	public void move() {
		updateInputs();
		long currentTime = System.currentTimeMillis();
		double delta = (double) (currentTime - m_lastUpdateTime)
				/ (double) 1000;
		if (rotation != 0) {
			rotate(rotation * Const.SHIP_ROTATION * delta);
			breakElapsed += delta;
			//scaleSpeed(Const.SHIP_TURN_BRAKE_SCALE);
		}
		super.move();
	}
	
	private void updateInputs() {
		if(m_keyConfig == null) return;
		switch(m_keyConfig.right) {
		case ON:
			rotation = 1;
			break;
		case OFF:
			if(rotation == 1) {
				rotation = 0;
			}
			break;
		default:
			break;
		}
		switch(m_keyConfig.left) {
		case ON:
			rotation = -1;
			break;
		case OFF:
			if(rotation == -1) {
				rotation = 0;
			}
			break;
		default:
			break;
		}
		switch(m_keyConfig.forward) {
		case ON:
			setAcceleration(Const.SHIP_MAX_ACCELERATION);
			break;
		case OFF:
			setAcceleration(0);
			break;
		default:
			break;
		}
	}

	@Override
	public void render(java.awt.Graphics g) {
		g.setColor(m_color);
		g.drawOval((int) getPosition().getX() - Const.SHIP_RADIUS,
				(int) getPosition().getY() - Const.SHIP_RADIUS,
				Const.SHIP_RADIUS * 2, Const.SHIP_RADIUS * 2);

		g.drawLine((int) getPosition().getX(), (int) getPosition().getY(),
				(int) (getPosition().getX() + getDirection().getX()
						* Const.SHIP_RADIUS),
				(int) (getPosition().getY() + getDirection().getY()
						* Const.SHIP_RADIUS));
	}

	@Override
	public boolean givesPoints() {
		return false;
	}

	@Override
	public double getRadius() {
		return Const.SHIP_RADIUS;
	}
	
	public int getRotation() {
		return rotation;
	}
	
	@Override
	public void updateTransformation(EntityTransformation t) {
		super.updateTransformation(t);
		rotation = t.rotation;
		if(m_speed.length() > m_lastSpeed.length()) {
			setAcceleration(Const.SHIP_MAX_ACCELERATION);
		} else {
			setAcceleration(0);
		}
		
	}
}