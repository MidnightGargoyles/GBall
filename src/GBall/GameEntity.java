package GBall;

import java.io.Serializable;
import java.util.Stack;

import shared.EntityTransformation;

public abstract class GameEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Vector2D m_position;
	private final Vector2D m_initialPosition;
	private final Vector2D m_initialDirection;
	protected final Vector2D m_speed;
	private final Vector2D m_direction; // Should always be unit vector;
										// determines the object's facing
	protected final Vector2D m_lastSpeed = new Vector2D();
	
	public final int id;
	
	private double m_acceleration; // Accelerates by multiplying this with
									// m_direction
	protected long m_lastUpdateTime;
	private double m_maxAcceleration;
	private double m_maxSpeed;
	private double m_friction;

	public abstract void render(java.awt.Graphics g);

	public abstract double getRadius();

	public abstract boolean givesPoints();

	public GameEntity(final Vector2D position, final Vector2D speed,
			final Vector2D direction, double maxAcceleration, double maxSpeed,
			double friction, int id) {
		m_position = position;
		m_speed = speed;
		m_direction = direction;
		m_maxAcceleration = maxAcceleration;
		m_friction = friction;
		m_maxSpeed = maxSpeed;
		m_acceleration = 0;
		m_lastUpdateTime = System.currentTimeMillis();
		m_initialPosition = new Vector2D(position.getX(), position.getY());
		m_initialDirection = new Vector2D(direction.getX(), direction.getY());
		this.id = id;
	}

	public void setAcceleration(double a) {
		if (a > m_maxAcceleration) {
			m_acceleration = m_maxAcceleration;
		} else if (a < (-m_maxAcceleration)) {
			m_acceleration = -m_maxAcceleration;
		} else
			m_acceleration = a;
	}

	protected double breakElapsed = 0.0;
	public double getBreakTime() {
		return breakElapsed;
	}
	public final Vector2D curPeak = new Vector2D();
	public void move() {
		// Change to per-frame movement by setting delta to a constant
		// Such as 0.017 for ~60FPS

		long currentTime = System.currentTimeMillis();
		double delta = (double) (currentTime - m_lastUpdateTime)
				/ (double) 1000;

		if (m_acceleration > 0) {
			changeSpeed(m_direction.multiplyOperator(m_acceleration * delta));
			
			
		} else {
			breakElapsed += delta;
			//Vector2D t = new Vector2D(m_speed);
			//t.setLength(delta * m_maxSpeed);
			//changeSpeed(t.multiplyOperator(-1));
			
		}
		scaleSpeed(Math.pow(0.55, breakElapsed));
		m_position.add(m_speed.multiplyOperator(delta));
		m_lastUpdateTime = currentTime;
	}

	public void scaleSpeed(double scale) {
		
		m_speed.set(curPeak.getX(), curPeak.getY());
		m_speed.scale(scale);
		if (m_speed.length() > m_maxSpeed) {
			m_speed.setLength(m_maxSpeed);
		}
		
	}

	public void changeSpeed(final Vector2D delta) {
		m_speed.add(delta);
		breakElapsed = 0;
		curPeak.set(m_speed.getX(), m_speed.getY());
		if (m_speed.length() > m_maxSpeed) {
			m_speed.setLength(m_maxSpeed);
		}
	}

	public void resetPosition() {
		m_position.set(m_initialPosition.getX(), m_initialPosition.getY());
		m_direction.set(m_initialDirection.getX(), m_initialDirection.getY());
		m_speed.set(0.0, 0.0);
		curPeak.set(0.0, 0.0);
	}

	public void deflectX() {
		m_speed.setX(-m_speed.getX());
		curPeak.setX(m_speed.getX());
	}

	public void deflectY() {
		m_speed.setY(-m_speed.getY());
		curPeak.setY(m_speed.getY());
	}

	public void rotate(double radians) {
		m_direction.rotate(radians);
	}

	public Vector2D getPosition() {
		return m_position;
	}

	public Vector2D getSpeed() {
		return m_speed;
	}

	public Vector2D getDirection() {
		return m_direction;
	}

	public void setPosition(double x, double y) {
		m_position.set(x, y);
	}

	public void displace(final Vector2D displacement) {
		m_position.add(displacement);
	}
	
	public void updateTransformation(EntityTransformation t) {
		m_lastSpeed.set(m_speed.getX(), m_speed.getY());
		m_speed.set(t.vel.getX(), t.vel.getY());
		curPeak.set(t.curPeak.getX(), t.curPeak.getY());
		if(m_speed.length() > m_lastSpeed.length())
			curPeak.set(m_speed.getX(), m_speed.getY());
		if(t.pos.minusOperator(m_speed).length() > Const.DD_FAULT_TOLERANCE) {
			m_position.set(t.pos.getX(), t.pos.getY());
		}
		
		m_direction.set(t.dir.getX(), t.dir.getY());
		
	}

}
